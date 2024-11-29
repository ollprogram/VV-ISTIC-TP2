package fr.istic.vv;

import com.github.javaparser.ast.type.Type;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class ReportWriter {

    private static class Entry{
        String clazz;
        String pack;
        String field;

        Type type;

        public Entry(String field, String clazz, String pack, Type type){
            this.clazz = clazz;
            this.pack = pack;
            this.field = field;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(clazz, entry.clazz) && Objects.equals(pack, entry.pack) && Objects.equals(field, entry.field) && Objects.equals(type, entry.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, pack, field, type);
        }
    }

    private final List<Entry> privateFields;

    private final List<Entry> getters;

    public ReportWriter(){
        privateFields = new ArrayList<>();
        getters = new ArrayList<>();
    }

    public void addField(String name, String clazz, String pack, Type type){
        privateFields.add(new Entry(name, clazz, pack, type));
    }

    public void checkGetter(String name, String clazz, String pack, Type type){
        StringBuilder target = new StringBuilder();
        if(name.startsWith("get") && name.length() > 3){
            target.append(name.substring(3));
            String firstChar = target.substring(0, 1);
            target.replace(0, 1, firstChar.toLowerCase());
        }
        Entry entryTarget = new Entry(target.toString(), clazz, pack, type);
        getters.add(entryTarget);
    }

    public void write(PrintStream out) throws IOException {
        for(Entry e : privateFields){
            boolean hasGetter = getters.contains(e);
            out.println("{Package = "+e.pack+", Class = "+e.clazz+", Field = "+e.field+", hasGetter = "+ hasGetter+"}\n");
        }
    }


}
