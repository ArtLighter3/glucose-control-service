//package com.artlighter.glucosecontrolservice;
//
//import com.tngtech.archunit.base.DescribedPredicate;
//import com.tngtech.archunit.core.domain.JavaClass;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.modulith.core.ApplicationModules;
//import org.springframework.modulith.docs.Documenter;
//
//@SpringBootTest
//public class SpringModulithTests {
//    @Test
//    public void createUML() {
//        DescribedPredicate<JavaClass> ignoredPackages = JavaClass.Predicates.resideInAnyPackage(
//                "com.artlighter.glucosecontrolservice.general.."
//        );
//
//        ApplicationModules modules = ApplicationModules.of(GlucoseControlServiceApplication.class, ignoredPackages);
//        new Documenter(modules)
//                .writeDocumentation()
//                .writeIndividualModulesAsPlantUml();
//    }
//}
