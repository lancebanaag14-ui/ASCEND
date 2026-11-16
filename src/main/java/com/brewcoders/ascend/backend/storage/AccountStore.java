package com.brewcoders.ascend.backend.storage;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import com.brewcoders.ascend.backend.model.Account;

/* 	Saves and loads account data while maintaining a previous-save backup.
 */
public class AccountStore {
    private final Path directory;
    private final Path file;

    // Choose the account storage folder and set the accounts.dat file path. */
    public AccountStore() {
        String custom = System.getProperty("ascend.dataDir");
        if (custom == null || custom.isBlank()) {
            directory = Paths.get(System.getProperty("user.home"), ".ascend");
        } else {
            directory = Paths.get(custom);
        }
        file = directory.resolve("accounts.dat");
    }

    // Return the full location of the account data file. */
    public String getFilePath() { return file.toAbsolutePath().toString(); }

    // Read saved accounts, reject invalid or incomplete data, and return an empty list if no save file exists.
    public ArrayList<Account> load() throws IOException {
        ArrayList<Account> accounts = new ArrayList<>();
        if (!Files.exists(file)) return accounts;
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            if (!"ASCEND-1".equals(input.readUTF())) throw new IOException("Unsupported account file version.");
            int count = input.readInt();
            if (count < 0 || count > 10000) throw new IOException("Invalid saved account count.");
            for (int i = 0; i < count; i++) {
                Account account = Account.readFrom(input);
                for (int j = 0; j < accounts.size(); j++) {
                    if (accounts.get(j).getUser().getUsername().equalsIgnoreCase(account.getUser().getUsername())) {
                        throw new IOException("Duplicate saved username.");
                    }
                }
                accounts.add(account);
            }
            if (input.read() != -1) throw new IOException("Unexpected data after saved accounts.");
        } catch (EOFException e) {
            throw new IOException("Saved account file is incomplete.", e);
        } catch (RuntimeException e) {
            throw new IOException("Invalid account data.", e);
        }
        return accounts;
    }

    //  Write accounts to a temporary file, back up the previous save, and replace the main account file with the new data.
    public void save(List<Account> accounts) throws IOException {
        Files.createDirectories(directory);
        Path temporary = directory.resolve("accounts.dat.tmp");
        Path backup = directory.resolve("accounts.dat.bak");
        try {
            try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(temporary)))) {
                output.writeUTF("ASCEND-1");
                output.writeInt(accounts.size());
                for (int i = 0; i < accounts.size(); i++) accounts.get(i).writeTo(output);
            }
            if (Files.exists(file)) Files.copy(file, backup, StandardCopyOption.REPLACE_EXISTING);
            try {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
