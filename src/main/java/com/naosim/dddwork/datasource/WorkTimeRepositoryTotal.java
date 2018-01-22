package com.naosim.dddwork.datasource;

import com.naosim.dddwork.domain.WorkTimeRepository;
import com.naosim.dddwork.domain.WorkTimeTotal;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WorkTimeRepositoryTotal implements WorkTimeRepository{
    @Override
    public WorkTimeTotal doExecute(String[] args) {
        String yearMonth = args[1];
        if(args.length < 2) {
            throw new RuntimeException("引数が足りません");
        }
        WorkTimeTotal workTimeTotal = null;

        int totalWorkMinutes = 0;
        int totalOverWorkMinutes = 0;

        File file = new File("data.csv");

        try(
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
        ) {

            String line = br.readLine();
            Map<String, Integer> totalWorkMinutesMap = new HashMap<>();
            Map<String, Integer> totalOverWorkMinutesMap = new HashMap<>();
            while(line != null){
                String[] columns = line.split(",");
                if(!columns[0].startsWith(yearMonth)) {
                    continue;
                }
                totalWorkMinutesMap.put(columns[0], Integer.valueOf(columns[3]));
                totalOverWorkMinutesMap.put(columns[0], Integer.valueOf(columns[4]));

                line = br.readLine();
            }

            Set<String> keySet = totalWorkMinutesMap.keySet();
            for(String key : keySet) {
                totalWorkMinutes += totalWorkMinutesMap.get(key);
                totalOverWorkMinutes += totalOverWorkMinutesMap.get(key);
            }

            workTimeTotal = new WorkTimeTotal();
            workTimeTotal.setTotalWorkMinutes(totalWorkMinutes);
            workTimeTotal.setTotalOverWorkMinutes(totalOverWorkMinutes);

            return workTimeTotal;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}