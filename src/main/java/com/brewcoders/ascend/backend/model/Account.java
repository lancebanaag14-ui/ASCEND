package com.brewcoders.ascend.backend.model;

import com.brewcoders.ascend.backend.security.Credential;
import com.brewcoders.ascend.model.Mission;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.brewcoders.ascend.backend.storage.MissionStorage;

/* Purpose: Keep one user's profile together with that user's password.
 */
public class Account {
    private final User user;
    private final Credential credential;

    public Account(String username, String password) {
        user = new User(username);
        credential = new Credential(password);
    }

    public User getUser() {
        return user;
    }

    public boolean matchesPassword(String password) {
        return credential.matches(password);
    }

    private Account(User user, Credential credential) {
        this.user = user;
        this.credential = credential;
    }

    /* Purpose: Write one account's profile, verifier, optional course and all missions.
     */
    public void writeTo(DataOutputStream output) throws IOException {
        output.writeUTF(user.getUsername());
        credential.writeTo(output);
        output.writeBoolean(user.hasCourse());
        if (user.hasCourse()) {
            CourseCycle course = user.getCurrentCourse();
            output.writeUTF(course.getCourseName());
            output.writeUTF(course.getStartDate().toString());
            output.writeUTF(course.getEndDate().toString());
        }
        List<Mission> missions = user.getMissions();
        output.writeInt(missions.size());
        for (int i = 0; i < missions.size(); i++) MissionStorage.write(missions.get(i), output);
    }

    /* Purpose: Validate a saved account before making it available to the service.
     */
    public static Account readFrom(DataInputStream input) throws IOException {
        User user = new User(input.readUTF());
        Credential credential = Credential.readFrom(input);
        if (input.readBoolean()) {
            user.createCourse(new CourseCycle(input.readUTF(),
                LocalDate.parse(input.readUTF()), LocalDate.parse(input.readUTF())));
        }
        int count = input.readInt();
        if (count < 0 || count > 100000) throw new IOException("Invalid saved mission count.");
        List<Mission> missions = new ArrayList<>();
        for (int i = 0; i < count; i++) missions.add(MissionStorage.read(input));
        user.setMissions(missions);
        return new Account(user, credential);
    }
}
