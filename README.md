# [![>bootenv](http://bootenv.com/img/logo-light-transparent-readme-files.png)](http://bootenv.com)-JAVA-UTILITY

[![license](https://img.shields.io/badge/license-Apache_2.0-blue.svg)]()
[![engine](https://img.shields.io/badge/JDK-v1.7+-yellow.svg)]()
[![gradle](https://img.shields.io/badge/gradle-v2.4-blue.svg)]()
[![Build Status](https://travis-ci.org/bootenv/bootenv-java-utility.svg?branch=master)](https://travis-ci.org/bootenv/bootenv-java-utility)

> It’s simple! Java utility methods to make using the environment properties more pleasant. `:wq`

## Prerequisites

You will need the following things properly installed on your computer.

* [Git](http://git-scm.com/)
* [Java](http://nodejs.org/)

## Installation

We use [Gradle](http://www.gradle.org), a cross-platform build automation tool that help with our full development flow. If you prefer [install Gradle](http://www.gradle.org/installation) or use a [Gradle wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html) inside this project.

* `git clone git@github.com:bootenv/bootenv-java-utility.git` this repository
* change into the new directory `bootenv-java-utility`

### Build project

```
./gradlew clean build
```

### Run tests

```
./gradlew clean test
```

### Generate Javadoc

```
./gradlew javaDoc
```

## Working with Environment Properties

*Environment* is a feature (idea based on [VRaptor’s feature](http://www.vraptor.org/)) that allow you to define different components and settings based on the Environment properties.

You can, for example, deactivate the e-mail service or set the email recipient loaded from Environment properties. __Awesome!__

### Getting Environment properties in your code

There are three usefull methods to get the Environment properties: 

- `Environment#getProperty(String)`
- `Environment#getProperty(String, String)` 
- `Environment#getOptionalProperty(String)`

The _first_ method returns the Environment property value if the value exists (_use only if you are 100% sure that the environment has a key_) if not found returns `null`.

The _second_ method returns the Environment property value if the key exists, otherwise returns the default value!

The _last_ method returns a [Optional](https://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained) property representation!

Please, take a look in the code below:

```java
@ApplicationScoped
public class MyClass {

    private final Environment environment;
    private final MailSender sender;

    private static final String SEND_EMAIL_PROPERTY = "SEND_EMAIL";
    private static final String EMAIL_ADDRESS_PROPERTY = "EMAIL_ADDRESS";
    private static final String DEFAULT_EMAIL_ADDRESS_PROPERTY = "DEFAULT_EMAIL_ADDRESS";
    
    private static final String DEFAULT_EMAIL_ADDRESS = "support@bootenv.org";

    private static final Logger LOG = getLogger(MyClass.class);

    /**
     * @deprecated CDI eyes only
     */
    @Deprecated
    protected MyClass() {
        this(null, null);
    }

    @Inject
    public MyClass(final Environment environment, final MailSender sender) {
        this.environment = environment;
        this.sender = sender;
    }

    public void sendMail() {
        if(environment.supports(SEND_EMAIL_PROPERTY, false)) {
            Optional<String> address = environment.getOptionalProperty(EMAIL_ADDRESS_PROPERTY);
            if(address.isPresent()){
                String email = address.get();
                LOG.info("Sending email to [{}]...", email);
                
                sender.sendMailTo(email);    
            } else {
                String email = environment.getProperty(DEFAULT_EMAIL_ADDRESS_PROPERTY, DEFAULT_EMAIL_ADDRESS);
                LOG.info("Sending email to default address [{}]...", email);
                
                sender.sendMailTo(email);
            }
        } else {
            LOG.info("Not email send!");
        }
    }
    
}


```

In the code above, `environment.getProperty(DEFAULT_EMAIL_ADDRESS_PROPERTY, DEFAULT_EMAIL_ADDRESS)` we will get the property email. But if this property is not always present, the  method returns the default value (`DEFAULT_EMAIL_ADDRESS = "support@bootenv.org"`).

## Further Reading / Useful Links

* [Gradle](http://gradle.org/)
* [Getting started with Gradle](http://gradle.org/getting-started-jvm/)
* [Google Guava](https://code.google.com/p/guava-libraries/wiki/GuavaExplained)

## Versions
 
 - 1.0.0 (current)

## License

[Apache-2.0](LICENSE)
