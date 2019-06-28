package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_EQUALS__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_HASHCODE__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import org.junit.Ignore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Ignore
public class TestObj extends EasyOverriderPreventingRecursiveToString<TestObj> {

    private static final EasyOverriderService easyOverriderService = new EasyOverriderServiceImpl();

    private boolean theBoolean;
    private int theInt;
    private String theString;
    private String theOtherString;
    private Collection<String> theCollectionString;
    private Map<String, Integer> theMapStringInt;
    private TestObj theTestObj;
    private List<TestObj> theCollectionTestObj;
    private Map<String, TestObj> theMapStringTestObj;

    private static final ParamList<TestObj> paramList =
                    ParamList.forClass(TestObj.class)
                             .usingService(easyOverriderService)
                             .allowingUnsafeParamMethodRestrictions()
                             .withParam("theBoolean", TestObj::isTheBoolean, IGNORED_FOR_EQUALS__UNSAFE, Boolean.class)
                             .withParam("theInt", TestObj::getTheInt, IGNORED_FOR_HASHCODE__UNSAFE, Integer.class)
                             .withParam("theString", TestObj::getTheString, String.class)
                             .withParam("theOtherString", TestObj::getTheOtherString, String.class)
                             .withCollection("theCollectionString", TestObj::getTheCollectionString, Collection.class, String.class)
                             .withMap("theMapStringInt", TestObj::getTheMapStringInt, Map.class, String.class, Integer.class)
                             .withParam("theTestObj", TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, TestObj.class)
                             .withCollection("theCollectionTestObj", TestObj::getTheCollectionTestObj, INCLUDED_IN_TOSTRING_ONLY, List.class, TestObj.class)
                             .withMap("theMapStringTestObj", TestObj::getTheMapStringTestObj, INCLUDED_IN_TOSTRING_ONLY, Map.class, String.class, TestObj.class)
                             .andThatsIt();

    @Override
    public ParamList<TestObj> getParamList() {
        return paramList;
    }

    public EasyOverriderService getEasyOverriderService() {
        return easyOverriderService;
    }

    public TestObj() { }

    public boolean isTheBoolean() {
        return theBoolean;
    }

    public void setTheBoolean(boolean theBoolean) {
        this.theBoolean = theBoolean;
    }

    public int getTheInt() {
        return theInt;
    }

    public void setTheInt(int theInt) {
        this.theInt = theInt;
    }

    public String getTheString() {
        return theString;
    }

    public void setTheString(String theString) {
        this.theString = theString;
    }

    public String getTheOtherString() {
        return theOtherString;
    }

    public void setTheOtherString(String theOtherString) {
        this.theOtherString = theOtherString;
    }

    public Collection<String> getTheCollectionString() {
        return theCollectionString;
    }

    public void setTheCollectionString(Collection<String> theCollectionString) {
        this.theCollectionString = theCollectionString;
    }

    public Map<String, Integer> getTheMapStringInt() {
        return theMapStringInt;
    }

    public void setTheMapStringInt(Map<String, Integer> theMapStringInt) {
        this.theMapStringInt = theMapStringInt;
    }

    public TestObj getTheTestObj() {
        return theTestObj;
    }

    public void setTheTestObj(TestObj theTestObj) {
        this.theTestObj = theTestObj;
    }

    public List<TestObj> getTheCollectionTestObj() {
        return theCollectionTestObj;
    }

    public void setTheCollectionTestObj(List<TestObj> theCollectionTestObj) {
        this.theCollectionTestObj = theCollectionTestObj;
    }

    public Map<String, TestObj> getTheMapStringTestObj() {
        return theMapStringTestObj;
    }

    public void setTheMapStringTestObj(Map<String, TestObj> theMapStringTestObj) {
        this.theMapStringTestObj = theMapStringTestObj;
    }
}
