# EasyOverrider
An easier, better way to override `equals(Object)`, `hashCode()`, and `toString()` in Java objects.

TODO: Completely update most, if not all of this due to recent changes.

TODO: Add a bit of an overview

## Installing / Getting started
TODO: Add publishing and write this up

For now, copy the `EasyOverrider/src/main/java` directory into your project and you're good to go.  Once I figure out a place to put it as a library, I'll update this with how to include it in your project.

## Developing
```
git clone https://github.com/SpicyLemon/EasyOverrider.git
cd EasyOverrider
```

### Building
This project uses gradle, and has a gradle wrapper.
```
./gradlew build
```
The above will build the project and run unit tests.
```
./gradlew tasks
```
The above will give you a list of commands you can run.

### Deploying / Publishing
TODO: Write this up

## Features
Here is an example class demonstrating the basic features of EasyOverrider.
```Java
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import Bar;
import EasyOverrider.ParamList;

public class Foo {
    private int id;
    private String name;
    private Bar bar;
    private String noGetter;

    private static final ParamList<Foo> paramList =
                      ParamList.forClass(Foo.class)
                               .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
                               .withParam("name", Foo::getName, String.class)
                               .withParam("bar", Foo::getBar, Bar.class)
                               .withParam("noGetter", (foo) -> foo.noGetter, String.class)
                               .andThatsIt();

    public Foo() { }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName { return name; }
    public void setName(String name} { this.name = name; }

    public Bar getBar { return bar; }
    public void setBar(Bar bar) { this.bar = bar; }

    public void setNoGetter(String noGetter) { this.noGetter = somethingWithoutAGetter; }

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
        return paramList.toString(this);
    }
}
```

There are a couple key pieces in the code above.  The first is the creation of the ParamList object. The second is the overriding of the `equals(Object)`, `hashCode()`, and `toString()` methods.

### Creation of a ParamList
Creation of a ParamList object is most easily done using a `ParamListBuilder`. The easiest way to create one is to is using the `ParamList.forClass(Class)` method. For most types of parameters, you can use the `withParam` methods. However, for `Collection` and `Map` objects, there are some special `withCollection` and `withMap` methods.
All of them have the same basic input: 
- The first argument is the name of the argument as a String. 
- The second argument is a method reference (or lambda) that defines how to get the parameter value.  In most cases, this will just be a reference to the getter. But if there is no getter for the parameter, you can use a lambda that takes in the object you're describing and outputs the parameter. 
- If there needs to be a restriction on which of the overridden methods the parameter is used for, the next argument can be a `ParamMethodRestriction` value. The default is `INCLUDED_IN_ALL`. If that's what's desired for this parameter, you can omit this argument.
- Then class information is needed.
  - For normal (single) parameters, all that's needed is the class of the parameter.
  - For Collection parameters, the class of colletion is needed (e.g. `Collection.class` or maybe `List.class`, anything that implements `Collection`), after that the class of the entries is needed. 
  - For Map parameters, the class of the map is needed (usually just `Map.class`), then the class of the keys, then the class of the values.
Once all the parameters have been defined, wrap up the builder with the `andThatsIt()` method. This method packages everything up and creates the final `ParamList` object.

#### Details of the ParamList class.
A `ParamList` is basically a collection of `ParamDescription` objects. `ParamDescription` is an interface, and most functionality is impleneted in the abstract class `ParamDescriptionBase`. Classes that extend that abstract class are `ParamDescriptionSingle`, `ParamDescriptionCollection`, and `ParamDescriptionMap`. More might be added later, but for now that's it. The reason special versions were needed for `Collection`s and `Map`s is because of a need to be able to prevent recursion.

The `ParamList` objects were designed to not be changeable once created. The idea here is that the ParamList represents things that are hard-coded and will not change dynamically. Basically, you don't generally programatically add and remove parameters from a class. Therefore, you shouldn't be able to alter the class the list represents, or the parameter descriptions defined in the object.

That being said, most functionality for both the `ParamDescription` and `ParamList` objects is controlled by the `EasyOverriderService`. The default implementation of which is `EasyOverriderServiceImpl`.  You can dictate the `EasyOverriderService` to use for a `ParamList` with the `usingService(EasyOverriderService)` method that is part of the `ParamListBuilder`. It is recommended that you make this is called right after the `forClass(Class)` line, but it's not required. If no other `EasyOverriderService` is defined, the default `EasyOverriderServiceImpl` is used.  If multiple calls to `usingService(EasyOverriderService)` are made, the last one is what sticks.

The `EasyOverriderService` also has a lot of options that allow you to more easily control the toString output.

Next, there are several `ParamMethodRestrictions` that end with `__UNSAFE`. Those entries are deemed unsafe because they cause a parameter to be used in a `hashCode()` method, but not an `equals(Object)` method.  This is usually not something that is desired, and can lead to strange and difficult-to-resolve consequences.  The rule is that if `objA.equals(objB)` then `objA.hashCode()` must equal `objB.hashCode()`. By including a parameter in equals, but not hashCode, there's a very good chance of breaking this part of the contract. Additionally, many things use hash codes and assume that if two hash codes are equal, the objects are the same. For example, a `HashSet<>` will do this. The code in `EasyOverrider` that prevents recursive `toString()` methods does this also. By default, if you set a parameter's `ParamMethodRestriction` to a value that ends in `__UNSAFE`, an `IllegalArgumentException` will be thrown. If you've encountered something that actually requires one of these `ParamMethodRestriction`s, you can, while creating the `ParamList`, set the `ParamMethodRestrictionRestriction` with the `havingRestriction(ParamMethodRestrictionRestriction)` method using the value `ALLOW_UNSAFE`. Alternatively you can also use the `allowingUnsafeParamMethodRestrictions()` method. This calls must be made prior to adding the parameter with the `__UNSAFE` `ParamMethodRestriction`.

Lastly, if you are extending a class that has a `ParamList` and you want to re-use it with alterations for your new class, you can use the `extendedBy(Class)` method to kick off a new `ParamListBuilder` starting with the info from the original `ParamList`.  The `extendedBy(Class)` method will copy the references to all the `ParamDescription` objects into a new `ParamListBuilder` while maintainting the parameter order. Additionally, the same `EasyOverriderService` is set in the new `ParamListBuilder` object.

#### Details of the ParamListBuilder.
The usual way of creating a `ParamListBuilder` is by using the static `ParamList.forClass(Class)` method. This way, you don't have to import the `ParamListBuilder` class into your class.

Once you have a `ParamListBuilder` there are several things you can do.
- Define the `EasyOverriderService` to use:
  - `usingService(EasyOverriderService)`: Sets the service that gets used for the core functionality of the resulting `ParamList` and `ParamDescription` objects. There's no need to call this multiple times in the creation of a `ParamList`, but if you do, the one executed last is the one that gets used. If this method is not used, a default `EasyOverriderServiceImpl` will be used for the new `ParamList`.
- Define the `ParamMethodRestrictionRestriction` that dictates which `ParamMethodRestriction` values are allowed:
  - `havingRestriction(ParamMethodRestrictionRestriction)`: Sets it to the provided value.
  - `allowingUnsafeParamMethodRestrictions()`: Sets it to `ALLOW_UNSAFE`.
  - `allowingOnlySafeParamMethodRestrictions()`: Sets it to `SAFE_ONLY`. This is the default state, though, so this method is only needed if you previously used one of the other two and want to switch back.
  - The default value is `SAFE_ONLY`. The restriction is only taken into account when adding or updating parameter entries using a `ParamListBuilder`. This is only significant when using the `extendedBy(Class)` method on a `ParamList` that contains `__UNSAFE` `ParamMethodRestrictions`. The previously created `ParamDescription`s are put in the new `ParamListBuilder` just fine. But adding a new one (or updating an existing one) with an `__UNSAFE` entry will throw an exception. Basically, any time a `ParamListBuilder` is created, the `ParamMethodRestrictionRestriction` is set to `SAFE_ONLY`.
- Add Parameters:
  - You can only add parameters that have not yet been defined. If the provided parameter name has already been defined in this `ParamListBuilder` then an `IllegalArgumentException` is thrown.
  - `withParam(String, Function, Class)`: Creates a `ParamDescriptionSingle` with the provided name that uses the provided function to get at the value, and has the provided class.  It uses the default `ParamMethodRestriction` of `INCLUDED_IN_ALL`.
  - `withParam(String, Function, ParamMethodRestriction, Class)`: This is the same as the above method except it uses the provided `ParamMethodRestriction`.
  - `withCollection(String, Function, Class, Class)`: Creates a `ParamDescriptionCollection` using the provided info with a default `ParamMethodRestriction` of `INCLUDED_IN_ALL`.
  - `withCollection(String, Function, ParamMethodRestriction, Class, Class)`: Same as above but using the provided `ParamMethodRestriction`.
  - `withMap(String, Function, Class, Class, Class)`: Creates a `ParamDescriptionMap` using the provided info and a default `ParamMethodRestriction` of `INCLUDED_IN_ALL`.
  - `withMap(String, Function, ParamMethodRestriction, Class, Class, Class)`: Same as above but using the provided `ParamMethodRestriction`.
  - When a `ParamDescription` is created using one of the above methods, it is added to the end of the list. This way, you have control over the order in which parameters are used for the various overridden methods.
- Change Parameters:
  - You can only change parameters that have already been defined. If the provided parameter name has not yet been defined in this `ParamListBuilder` then an `IllegalArgumentException` is thrown.
  - The `ParamDescription` objects are never actually updated. Instead, a new one is created and the reference to it in this `ParamListBuilder` is changed to the newly created one.
  - `withUpdatedParam(String, Function, Class)`: Creates a `ParamDescriptionSingle` with the provided info and default `ParamMethodRestriction` of `INCLUDED_IN_ALL`.
  - `withUpdatedParam(String, Function, ParamMethodRestriction, Class)`: Same as above except using the provided `ParamMethodRestriction` instead of the default.
  - `withUpdatedCollection(String, Function, Class, Class)`: Creates a `ParamDescriptionCollection` with the provided info and `ParamMethodRestriction` of `INCLUDED_IN_ALL`.
  - `withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)`: Same as above except using the provided `ParamMethodRestriction`.
  - `withUpdatedMap(String, Function, Class, Class, Class)`: Creates a `ParamDescriptionMap` with the provided info and `ParamMethodRestriction` of `INCLUDED_IN_ALL`.
  - `withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)`: Same as above except using the provided 
  `ParamMethodRestriction` instead of the default.
  - All of these update methods do not alter the parameter's position in the ordering. For example, if you've got 10 parameters and update the 3rd one, it will remain 3rd, but have the updated `ParamDescription`.
- Remove Parameters:
  - `withoutParam(String)`: Removes the parameter with the given name. If no parameter exists with that name, an `IllegalArgumentException` is thrown.
- Finalize the builder and get the resulting `ParamList` object.
  - `andThatsIt()`: This packages everything up and constructs the desired `ParamList` object.

### Overriding the Object methods.
```Java
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
    return paramList.toString(this);
}
```
The above code is basically all you need in order to override the `equals(Object)`, `hashCode()`, and `toString()` methods.

#### The equals(Object) method.
The `ParamList.equals(Object, Object)` method executes a standard object comparison.
1. It firsts test equality using `==`. If they're equal, returns true.
2. Then, if either one is null, returns false.
3. Then it checks if the objects are instances of the class of the ParamList (the one defined using `forClass(Class)`).
   - If neither are instances, make return `obj1.equals(obj2)`.
   - If only one is, return false.
4. Then go through each entry in the `ParamList` that has a `ParamMethodRestriction` indicating the paremeter should be used in the `equals()` method.
5. Use the getter defined for the parameter to get the value from each object.
   - If the two are NOT equal using either `==` or `Objects.equals(Object, Object)`, return false.
   - Otherwise, move on to the next parameter in the list.
6. If all defined parameters are equal, return true.

Using the class defined in the Features section as an example, this method:
```Java
@Override
public boolean equals(Object obj) {
    return paramList.equals(this, obj);
}
```
is equivalent to the following:
```Java
@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(o instanceof Foo) {
        return false;
    }
    Foo that = (Foo)obj;
    return Objects.equals(this.getName(), that.getName()) &&
           Objects.equals(this.getBar(), that.getBar()) &&
           Objects.equals(this.noGetter, that.noGetter);
}
```

#### The hashCode() method.
The `ParamList.hashCode(Object)` method creates a hash code of the object in a standard way.
1. Go through each entry in the `ParamList` that has a `ParamMethodRestriction` indicating the parameter should be used in the `hashCode()` method.
2. Use the getter defined for each parameter to get the values of each.
3. Put all the values in an array of Objects.
4. Pass the array into the `Objects.hash(Object[])` method, and return its result.

Using the class defined in the Features section as an example, this method:
```Java
@Override
public int hashCode() {
    return paramList.hashCode(this);
}
```
is equivalent to the following:
```Java
@Override
public int hashCode() {
    return Objects.hash(this.getName(), this.getBar(), this.noGetter);
}
```

So the `hashCode()` method using a `ParamList` doesn't save much code space. However, it does make it easier to keep the parameters involved in the `hashCode()` method in sync with the parameters involved in the `equals(Object)` method. In fact, you have to jump through some hoops in order to add a parameter that is included in one but not the other (more on that later).

#### The toString() method.
The `ParamList.toString(Object)` method creates a standard String representation of an object. The general format of the return value is `"ClassName@HexedHashCode [name1='value1', name2=null, ...]"`.
1. Create the string of the class name.
2. Use the object's `hashCode()` method and convert it to a Hex string.
3. Go through each entry in the ParamList that has a ParamMethodRestriction indicating the parameter should be used in the `toString()` method.
4. Create a name/value string for each parameter.
   - If the parameter is a class that implements `RecursionPreventingToString` and recursion is detected (the value has already been converted to a string to be included in this output), `...` is the value used. E.g. `"Bar=..."`.
   - If the parameter's value is null, it will just be `null` in the string (no quotes). E.g. `"name=null"`.
   - If the parameter's value is not null, it will be surrounded in single quotes. E.g. `"id='3'"`.
5. Combine the name/value strings into one long string using a delimiter of `", "`.
6. Combine the class name String, hexed hash code String, and parameters string using the `String.format` of `"%1$s@%2$s [%3$s]"` where 1 is the class name, 2 is the hexed hash code, and 3 is the parameters.

An example `Foo.toString()`: `"Foo@239ca93d [id='1', name='Danny', bar=null, noGetter='I got gotten']"`

Using the class defined in the Features section as an example, this method:
```Java
@Override
public String toString() {
    return paramList.toString(this);
}
```
is equivalent to the following:
```Java
@Override
public String toString() {
  return "Foo@" + Integer.toHexString(hashCode()) + " [" +
         "id='" + getId() + "'" + ", " +
         "name=" + (getName() != null ? "'" + getName() + "'" : "null") + ", " +
         "bar=" + (getBar() != null ? "'" + getBar().toString() + "'" : "null") + ", " +
         "noGetter=" + (noGetter != null ? "'" + noGetter + "'" : "null") + 
         "]";`
}
```

#### Preventing recursive toString() methods.
It is not uncommon for an object or chain of objects to have circular references. A common example of this is found in some Hibernate models.  Object A has a list of Object B, and Object B has a reference back to Object A.  If the naive approach is used for a `toString()` method, though, you end up with an infinitely recursive `toString()` method. Using the previous example, the `toString()` method of Object A would call `listBs.toString()` which would include a call to the `toString()` in Object B. And the `toString()` of Object B would make a call to `myA.toString()`. And it would never stop.

In order to prevent this, any class that might be involved in a infinitely recursive `toString()` call should implement the `RecursionPreventingToString` interface.  The `ParamList` behavior is to look for things that implement this interface, record the objects that have been seen, and prevent a call to a `toString()` that has already been done.

The implementation of the interface is simple. There's just one function to implement and it will look something like this:
```Java
@Override
public String toString(final Map<Class, Set<Integer>> seen) {
    return paramList.toString(this, seen);
}
```

If a `ParamDescriptionSingle` describes a parameter that implements `RecursionPreventingToString`, prior to calling `toString(Map)` on the object, the object's hash code is calculated. If it's already in the provided `Map`, the call is prevented. Otherwise, it is added to the `Map` and passed on to the next `toString(Map)`.

If a `ParamDescriptionCollection` describes a parameter whose entries implement `RecursionPreventingToString`, each entry is treated the same was as a `ParamDescriptionSingle`.

For `ParamDescriptionMap` parameters, both the key and value pieces are checked to see if they implement `RecursionPreventingToString`.

### Pre-built abstract classes for you to extend
If your class extends the `EasyOverrider` abstract class, then the `equals(Object)`, `hashCode()`, and `toString()` methods are automatically overridden for you to use a ParamList object. the stipulation is that you have to implement a `getParamList()` method.

Here's the `Foo.class` again, but extending the `EasyOverrider` abstract class.
```Java
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import Bar;
import EasyOverrider.ParamList;
import EasyOverrider.EasyOverrider;

public class Foo extends EasyOverrider<Foo> {
    private int id;
    private String name;
    private Bar bar;
    private String noGetter;

    private static final ParamList<Foo> paramList =
                      ParamList.forClass(Foo.class)
                               .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
                               .withParam("name", Foo::getName, String.class)
                               .withParam("bar", Foo::getBar, Bar.class)
                               .withParam("noGetter", (foo) -> foo.noGetter, String.class)
                               .andThatsIt();

    ParamList<Foo> getParamList() {
        return paramList;
    }

    public Foo() { }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName { return name; }
    public void setName(String name} { this.name = name; }

    public Bar getBar { return bar; }
    public void setBar(Bar bar) { this.bar = bar; }

    public void setNoGetter(String noGetter) { this.noGetter = somethingWithoutAGetter; }
}
```

The `EasyOverriderPreventingRecursiveToString` abstract class extends the `EasyOverrider` class and implements the `RecursionPreventingToString` interface. Here again, in order to use it, you have to implement the `getParamList()` method. However, the `toString(Map)` method is now already implemented, so you don't have to worry about that.

If your class cannot be extended, though, you'll have to just do it the "hard" way previously discussed.

## Configuration
TODO: Add notes on configuration through the `EasyOverriderServiceImpl`.

## Contributing
Feature requests are warmly welcome.
Pull requests are even more welcome!

## Links
Project homepage: https://github.com/SpicyLemon/EasyOverrider
Repository: https://github.com/SpicyLemon/EasyOverrider.git
Issue tracker: https://github.com/SpicyLemon/EasyOverrider/issues

## Licensing

The code in this project is licensed under the (MIT license)[LICENSE]
