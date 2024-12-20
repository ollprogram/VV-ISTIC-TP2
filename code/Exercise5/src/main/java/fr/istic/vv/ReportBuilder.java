package fr.istic.vv;

import java.io.PrintStream;
import java.util.*;

public class ReportBuilder {

    private static class CCStat{
        String method;
        int cc;
        public CCStat(String method, int cc){
            this.cc = cc;
            this.method = method;
        }

    }

    private class Entry{
        String pack;
        String clazz;
        public Entry(String pack, String clazz){
            this.pack = pack;
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(pack, entry.pack) && Objects.equals(clazz, entry.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pack, clazz);
        }
    }

    private final Map<Entry, List<CCStat>> statsMap;

    public ReportBuilder(){
        statsMap = new HashMap<>();
    }

    public void addCC(String pack, String clazz, String method, int cc){
        Entry e = new Entry(pack, clazz);
        List<CCStat> stats = statsMap.get(e);
        CCStat stat = new CCStat(method, cc);
        if(stats == null) {
            stats = new ArrayList<>();
            stats.add(stat);
            statsMap.put(e, stats);
        }
        else {
            stats.add(stat);
        }
    }

    private String getScoreHist(int score){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < score; i++){
            builder.append("█");
        }
        builder.append(" -> (").append(score).append(")");
        return builder.toString();
    }

    public void writeReport(PrintStream stream){
        stream.println("REPORT");
        for(Entry e: statsMap.keySet()) {
            stream.println("------- "+e.pack+"."+e.clazz+" -------");
            for (CCStat stat: statsMap.get(e)) {
                stream.println(stat.method);
                stream.println(getScoreHist(stat.cc));
                stream.println();
            }
        }
    }

}
