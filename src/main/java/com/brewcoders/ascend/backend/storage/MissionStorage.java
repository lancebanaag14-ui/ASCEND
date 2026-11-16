package com.brewcoders.ascend.backend.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import com.brewcoders.ascend.model.Mission;
import com.brewcoders.ascend.backend.service.MissionFactory;

/*	Writes and restores mission details and statuses in the saved account file.
 */
public class MissionStorage {

    private MissionStorage() {}

    //  Purpose: Write the mission details to the account save file.
    public static void write(Mission mission, DataOutputStream output) throws IOException {
        output.writeUTF(mission.getName());
        output.writeUTF(mission.getDescription());
        output.writeUTF(mission.getDueDate().toString());
        output.writeUTF(mission.getCategory());
        output.writeUTF(mission.getDifficulty());
        output.writeUTF(mission.getStatus());
    }
    
    // Purpose: Read the mission details from the account save file and return a new mission status.
    public static Mission read(DataInputStream input) throws IOException {
        String name = input.readUTF();
        String notes = input.readUTF();
        LocalDate due = LocalDate.parse(input.readUTF());
        String category = input.readUTF();
        String difficulty = input.readUTF();
        String status = input.readUTF();
        if (!"ACTIVE".equals(status) && !"COMPLETED".equals(status) && !"FAILED".equals(status)) {
            throw new IOException("Invalid saved mission status.");
        }
        Mission mission = MissionFactory.create(name, notes, due, category, difficulty);
        mission.setStatus(status);
        return mission;
    }
}
