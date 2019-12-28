package umlclass;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import umlclass.classorinterface.MyUmlClass;
import umlclass.classorinterface.MyUmlInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassQuery {
    private HashMap<String, UmlElement> idToElement; // id 映射到元素
    private HashMap<String, MyUmlClass> nameToClass; // 一个元素的name映射到该元素
    private HashMap<String, MyUmlClass> idToMyClass; // id到类
    private HashMap<String, Integer> classToNum;  // 记录一个类名被使用的次数
    private HashMap<String, MyUmlInterface> idToMyInterface; // name到接口
    private HashMap<String, UmlAssociation> associationHashMap; // id映射到关联关系
    private HashMap<String, UmlAssociationEnd> associationEndHashMap;
    private HashMap<String, UmlClass> classHashMap; // id映射到类
    private HashMap<String, UmlAttribute> attributeHashMap; // id映射到属性
    private HashMap<String, UmlGeneralization> generalizationHashMap;
    private HashMap<String, UmlInterface> interfaceHashMap; // id映射到接口
    private HashMap<String, UmlInterfaceRealization> realizationHashMap;
    private HashMap<String, UmlOperation> operationHashMap; // id映射到方法
    private HashMap<String, UmlParameter> parameterHashMap;// id映射到参数
    private HashMap<String, Integer> idToColor; // 一个类id对应一种颜色
    private HashMap<Integer, MyUmlClass> colorToClass; // 一种颜色对应一个顶级父类
    private Integer color; // dfs染色使用
    private Check check;
    
    public ClassQuery(UmlElement... elements) {
        idToElement = new HashMap<>();
        nameToClass = new HashMap<>();
        idToMyClass = new HashMap<>();
        classToNum = new HashMap<>();
        associationEndHashMap = new HashMap<>();
        associationHashMap = new HashMap<>();
        classHashMap = new HashMap<>();
        attributeHashMap = new HashMap<>();
        generalizationHashMap = new HashMap<>();
        idToMyInterface = new HashMap<>();
        interfaceHashMap = new HashMap<>();
        realizationHashMap = new HashMap<>();
        operationHashMap = new HashMap<>();
        parameterHashMap = new HashMap<>();
        idToColor = new HashMap<>();
        colorToClass = new HashMap<>();
        color = 0;
        check = new Check(idToMyClass, interfaceHashMap,
                idToElement, idToMyInterface, classHashMap);
        for (UmlElement element : elements) {
            putEle(element);
        }
        setUpAll();
    }
    
    private void putEle(UmlElement element) {
        idToElement.put(element.getId(), element);
        switch (element.getElementType()) {
            case UML_CLASS:
                UmlClass umlClass = (UmlClass) element;
                classHashMap.put(element.getId(), umlClass);
                if (classToNum.containsKey(umlClass.getName())) {
                    classToNum.put(umlClass.getName()
                            , classToNum.get(umlClass.getName()) + 1);
                } else {
                    classToNum.put(umlClass.getName(), 1);
                }
                break;
            case UML_ASSOCIATION:
                associationHashMap.put(element.getId(),
                        (UmlAssociation) element);
                break;
            case UML_ASSOCIATION_END:
                associationEndHashMap.put(element.getId(),
                        (UmlAssociationEnd) element);
                break;
            case UML_ATTRIBUTE:
                attributeHashMap.put(element.getId(), (UmlAttribute) element);
                break;
            case UML_OPERATION:
                operationHashMap.put(element.getId(), (UmlOperation) element);
                break;
            case UML_PARAMETER:
                parameterHashMap.put(element.getId(), (UmlParameter) element);
                break;
            case UML_GENERALIZATION:
                generalizationHashMap.put(element.getId(),
                        (UmlGeneralization) element);
                break;
            case UML_INTERFACE_REALIZATION:
                realizationHashMap.put(element.getId(),
                        (UmlInterfaceRealization) element);
                break;
            case UML_INTERFACE:
                interfaceHashMap.put(element.getId(), (UmlInterface) element);
                break;
            default:
                break;
        }
    }
    
    private void setUpAll() {
        setUpClass();
        setUpInterface();
        setUpAttributes();
        setUpAssociate();
        setUpOperation();
        setUpGeneralize(); // 之后应该设置顶级父类
        //setUpTopInterface();
        //setUpRealization(); //之后应该设置某个类实现的所有接口
        //setUpTopClass();
        setUpPara();
    }
    
    private void setUpClass() {
        classHashMap.values().forEach(umlClass -> {
            MyUmlClass myUmlClass = new MyUmlClass(umlClass);
            nameToClass.put(umlClass.getName(), myUmlClass); // 如果有重名的话会被覆盖
            idToMyClass.put(umlClass.getId(), myUmlClass);
        });
    } // 类
    
    private void setUpInterface() {
        interfaceHashMap.values().forEach(umlInterface -> {
            MyUmlInterface myUmlInterface = new MyUmlInterface(umlInterface);
            idToMyInterface.put(umlInterface.getId(), myUmlInterface);
        });
    }
    
    private void setUpAttributes() {
        // 接口也可以包含属性
        for (UmlAttribute attribute : attributeHashMap.values()) {
            String parentId = attribute.getParentId();
            UmlElement element = idToElement.get(parentId);
            if (element != null &&
                    element.getElementType() == ElementType.UML_CLASS) {
                MyUmlClass myUmlClass = idToMyClass.get(parentId);
                if (myUmlClass != null) {
                    // 顺序图中role的type也是UMLAttribute
                    myUmlClass.addAttribute(attribute);
                }
            }
        }
    } // 属性
    
    private void setUpAssociate() {
        for (UmlAssociation association : associationHashMap.values()) {
            String end1Id = association.getEnd1();
            String end2Id = association.getEnd2();
            UmlAssociationEnd end1 = associationEndHashMap.get(end1Id);
            UmlAssociationEnd end2 = associationEndHashMap.get(end2Id);
            String end1RefId = end1.getReference();
            String end2RefId = end2.getReference();
            if (classHashMap.containsKey(end1RefId)) {
                MyUmlClass class1 = idToMyClass.get(end1RefId);
                class1.addAssociate(idToElement.get(end2RefId));
                class1.addAssoEnd(end2);
            }
            if (classHashMap.containsKey(end2RefId)) {
                MyUmlClass class2 = idToMyClass.get(end2RefId);
                class2.addAssociate(idToElement.get(end1RefId));
                class2.addAssoEnd(end1);
            }
        }
    } //关联
    
    private void setUpOperation() {
        for (UmlOperation operation : operationHashMap.values()) {
            String classId = operation.getParentId();
            UmlElement parent = idToElement.get(classId); // 有可能是接口
            switch (parent.getElementType()) {
                case UML_CLASS:
                    idToMyClass.get(parent.getId()).addOperation(operation);
                    break;
                case UML_INTERFACE:
                    idToMyInterface.get(parent.getId()).addOperation(operation);
                    break;
                default:
                    break;
            }
        }
    } // 方法
    
    private void setUpPara() {
        parameterHashMap.values().forEach(para -> {
            UmlOperation ope = operationHashMap.get(
                    para.getParentId()); // 得到该参数存在的方法
            // 得到该参数的方向：输入/输出
            Direction direction = para.getDirection();
            // 得到这个方法所在的类
            UmlElement element = idToElement.get(ope.getParentId());
            // 得到保存这个方法的类
            switch (element.getElementType()) {
                case UML_CLASS:
                    MyUmlClass myClass = idToMyClass.get(element.getId());
                    myClass.addOperation(ope, direction);
                    break;
                case UML_INTERFACE:
                    break;
                default:
                    break;
            }
        });
    } // 设置所有方法参数
    
    private void setUpGeneralize() {
        generalizationHashMap.values().forEach(generalization -> {
            String childId = generalization.getSource();
            String parentId = generalization.getTarget();
            UmlElement parent = idToElement.get(parentId);
            UmlElement child = idToElement.get(childId);
            switch (parent.getElementType()) {
                case UML_CLASS:
                    MyUmlClass parentC = idToMyClass.get(parentId);
                    MyUmlClass childC = idToMyClass.get(childId);
                    parentC.setChild((UmlClass) child);
                    childC.setParent((UmlClass) parent);
                    break;
                case UML_INTERFACE:
                    MyUmlInterface iiParent = idToMyInterface.get(parentId);
                    MyUmlInterface iiChild = idToMyInterface.get(childId);
                    iiChild.setParents((UmlInterface) parent);
                    iiParent.setChildren((UmlInterface) child);
                    break;
                default:
                    break;
            }
        });
    } // 继承关系
    
    private void setUpTopInterface() {
        List<String> vis = new ArrayList<>();
        for (MyUmlInterface myUmlInterface : idToMyInterface.values()) {
            dfsInt(myUmlInterface, vis);
        }
    } // 建立接口继承
    
    private void dfsInt(MyUmlInterface i, List<String> vis) {
        if (vis.contains(i.getId())) {
            return;
        } else if (i.getDirparents().size() == 0) {
            // TOP
            vis.add(i.getId());
            return;
        }
        for (UmlInterface dirParent : i.getDirparents().values()) {
            MyUmlInterface parentInt = idToMyInterface.get(dirParent.getId());
            dfsInt(parentInt, vis);
            i.addGenNum(parentInt.getId2GenNum());
            i.addParents(parentInt.getDirparents());
            i.addParents(parentInt.getParents());
            vis.add(i.getId());
        }
    }
    
    private void setUpRealization() {
        realizationHashMap.values().forEach(realization -> {
            String interefaceId = realization.getTarget();
            String classId = realization.getSource();
            UmlClass umlClass = classHashMap.get(classId);
            UmlInterface umlInterface = interfaceHashMap.get(interefaceId);
            MyUmlClass myClass = idToMyClass.get(umlClass.getId());
            myClass.addIntereface(umlInterface);
            myClass.addMyInterface(idToMyInterface.get(interefaceId));
        });
    } // 建立接口实现关系
    
    private void setUpTopClass() {
        color = 0;
        for (MyUmlClass myClass : idToMyClass.values()) {
            dfsDye(myClass);
        }
    } // 建立顶级父类
    
    private void dfsDye(MyUmlClass myClass) {
        if (idToColor.containsKey(myClass.getId())) {
            return;
        }
        if (myClass.getParent() == null) {
            // 说明myClass就是顶级父类
            if (!colorToClass.values().contains(myClass)) {
                myClass.setAttrClassMap();
                idToColor.put(myClass.getId(), color);
                colorToClass.put(color, myClass);
                color++;
            }
            return;
        }
        UmlClass parent = myClass.getParent();
        MyUmlClass myParent = idToMyClass.get(parent.getId());
        dfsDye(idToMyClass.get(parent.getId()));
        // 把父类的信息传递给子类
        parPassToChild(myClass, myParent);
    }
    
    private void parPassToChild(MyUmlClass myClass, MyUmlClass myParent) {
        // 把父类的颜色赋给子类
        Integer parentColor = idToColor.get(myParent.getId());
        idToColor.put(myClass.getId(), parentColor);
        // 把父类的属性和关联加进来
        myClass.addParentAssoEle(myParent.getAssociate());
        myClass.addParentAssoEle(myParent.getParentAsso());
        // 把父类关联到的类加进来
        myClass.addParentAssoClass(myParent.getAssoClass());
        //把父类关联关系中的end加进来
        myClass.addParentAssoEnd(myParent.getAssoEndList());
        // 加入父类的属性
        myClass.addAttribute(myParent.getAttribute());
        myClass.addAttribute(myParent.getParentAttr());
        // 加入父类“属性类”
        myClass.setAttrClassMap(myParent.getAttrClassMap());
        // 将父类实现的接口加入子类
        myClass.addParentIntereface(myParent.getInterfaceMap());
        // 将父类实现的我的接口加入子类
        myClass.addParentMyInt(myParent.getMyInterfaceMap());
        // 把父类的实现次数加入
        myClass.addRealNum(myParent.getId2RealNum());
    }
    
    private void checkException(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        if (!nameToClass.containsKey(s)) {
            throw new ClassNotFoundException(s);
        } else if (classToNum.get(s) > 1) {
            throw new ClassDuplicatedException(s);
        }
    }
    
    public int getClassCount() {
        return classHashMap.size();
    }
    
    public int getClassOperationCount(
            String s, OperationQueryType operationQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        int res = 0;
        switch (operationQueryType) {
            case NON_RETURN:
                res = myClass.getOperation().size()
                        - myClass.getReturnOptMap().size();
                break;
            case RETURN:
                res = myClass.getReturnOptMap().size();
                break;
            case NON_PARAM:
                res = myClass.getOperation().size()
                        - myClass.getInOptMap().size();
                break;
            case PARAM:
                res = myClass.getInOptMap().size();
                break;
            case ALL:
                res = myClass.getOperation().size();
                break;
            default:
                break;
        }
        return res;
    }
    
    public int getClassAttributeCount(
            String s, AttributeQueryType attributeQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        int res = 0;
        switch (attributeQueryType) {
            case ALL:
                res = myClass.getAttribute().size()
                        + myClass.getParentAttr().size();
                break;
            case SELF_ONLY:
                res = myClass.getAttribute().size();
                break;
            default:
                break;
        }
        return res;
    }
    
    public int getClassAssociationCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        return myClass.getClassAssociationCount();
        
    } // 关联了多少个对象
    
    public List<String> getClassAssociatedClassList(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        checkException(s);
        return nameToClass.get(s).getAssoList();
    } // 关联到哪些类
    
    public Map<Visibility, Integer> getClassOperationVisibility(
            String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        if (myClass.getNameOpVisMap().get(s1) == null) {
            return new HashMap<>();
        }
        return myClass.getNameOpVisMap().get(s1);
    }
    
    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        if (!myClass.getAttrNameToNum().containsKey(s1)) {
            throw new AttributeNotFoundException(s, s1);
        } else if (myClass.getAttrNameToNum().get(s1) > 1) {
            throw new AttributeDuplicatedException(s, s1);
        } else {
            return myClass.getNameToVis().get(s1);
        }
    }
    
    public String getTopParentClass(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        MyUmlClass topClass = colorToClass.get(
                idToColor.get(myClass.getId()));
        return topClass.getName();
    }
    
    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        return myClass.getImplementInterfaceList();
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassDuplicatedException, ClassNotFoundException {
        checkException(s);
        MyUmlClass myClass = nameToClass.get(s);
        return myClass.getInfoList();
    }
    
    public void checkForUml002() throws UmlRule002Exception {
        check.checkForUml002();
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        check.checkForUml008();
    }
    
    public void checkForUml009() throws UmlRule009Exception {
        setUpTopInterface(); // 建立好接口的继承
        setUpRealization(); // 建立接口的实现
        setUpTopClass(); // 建立子类的继承
        check.checkForUml009();
    }
}
