package com.brewcoders.ascend.backend.service;

import java.util.ArrayList;
import java.util.List;
import com.brewcoders.ascend.model.Mission;

/* Purpose: Sort missions by nearest deadline using insertion sort.
 */
public class MissionSorter {
   
    private MissionSorter() {}

    public static List<Mission> byDueDate(List<Mission> missions) {
        List<Mission> sorted = new ArrayList<>(missions);
        for (int i = 1; i < sorted.size(); i++) {
            Mission selected = sorted.get(i);
            int j = i - 1;
            while (j >= 0 && sorted.get(j).getDueDate().isAfter(selected.getDueDate())) {
                sorted.set(j + 1, sorted.get(j));
                j--;
            }
            sorted.set(j + 1, selected);
        }
        return sorted;
    }
}
