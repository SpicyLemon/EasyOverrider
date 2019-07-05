package EasyOverrider.TestingUtils;

import static EasyOverrider.ParamUsage.HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.EQUALS_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.TOSTRING_ONLY;
import static EasyOverrider.TestingUtils.Helpers.getConfig;

import EasyOverrider.ParamList;
import EasyOverrider.ParamListServiceConfig;
import EasyOverrider.ParamListServiceImpl;
import EasyOverrider.RecursionPreventingToString;
import org.junit.Ignore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Ignore
public class TestObj implements RecursionPreventingToString {

    private boolean theBoolean;
    private int theInt;
    private String theString;
    private String theOtherString;
    private Collection<String> theCollectionString;
    private Map<String, Integer> theMapStringInt;
    private TestObj theTestObj;
    private List<TestObj> theCollectionTestObj;
    private Map<String, TestObj> theMapStringTestObj;

    private final ParamList<TestObj> paramList;

    private ParamList<TestObj> createParamList(ParamListServiceConfig config) {
        return ParamList.forClass(TestObj.class)
                        .usingService(new ParamListServiceImpl(config))
                        .allowingUnsafeParamUsages()
                        .withParam("theBoolean", TestObj::isTheBoolean, HASHCODE_AND_TOSTRING_ONLY__UNSAFE, Boolean.class)
                        .withPrimaryParam("theInt", TestObj::getTheInt, EQUALS_AND_TOSTRING_ONLY__UNSAFE, Integer.class)
                        .withParam("theString", TestObj::getTheString, String.class)
                        .withParam("theOtherString", TestObj::getTheOtherString, String.class)
                        .withCollection("theCollectionString", TestObj::getTheCollectionString, Collection.class, String.class)
                        .withMap("theMapStringInt", TestObj::getTheMapStringInt, Map.class, String.class, Integer.class)
                        .withParam("theTestObj", TestObj::getTheTestObj, TOSTRING_ONLY, TestObj.class)
                        .withCollection("theCollectionTestObj", TestObj::getTheCollectionTestObj, TOSTRING_ONLY, List.class, TestObj.class)
                        .withMap("theMapStringTestObj", TestObj::getTheMapStringTestObj, TOSTRING_ONLY, Map.class, String.class, TestObj.class)
                        .andThatsIt();
    }

    public TestObj() {
        paramList = createParamList(getConfig());
    }

    public TestObj(ParamListServiceConfig config) {
        paramList = createParamList(config);
    }

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

    @Override
    public boolean equals(Object obj) {
        return paramList.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return paramList.hashCode(this);
    }

    @Override
    public String toString() {
        return paramList.toString(this, null);
    }

    @Override
    public String toString(Map<Class, Set<Integer>> seen) {
        return paramList.toString(this, seen);
    }

    @Override
    public String primaryToString() {
        return paramList.primaryToString(this);
    }
}
