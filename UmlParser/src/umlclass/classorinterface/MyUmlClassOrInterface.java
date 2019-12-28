package umlclass.classorinterface;

import java.util.Set;

public abstract class MyUmlClassOrInterface {
    public abstract String getId();
    
    @Override
    public abstract boolean equals(Object obj);
    
    public abstract Set<String> getParentsId();
}
