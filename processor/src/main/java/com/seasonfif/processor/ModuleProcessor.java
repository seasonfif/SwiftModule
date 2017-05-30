package com.seasonfif.processor;

import com.google.auto.service.AutoService;
import com.seasonfif.annotation.Through;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class ModuleProcessor extends AbstractProcessor{

    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(Through.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Through.class);
        List<VariableElement> fields = new ArrayList<>();
        TypeElement typeElement = null;
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS){

                typeElement = (TypeElement) element;
                System.out.println(1 + typeElement.getSimpleName().toString());
            } else if(element.getKind() == ElementKind.INTERFACE){

                typeElement = (TypeElement) element;
                System.out.println(2 + typeElement.getSimpleName().toString());

            } else if(element.getKind() == ElementKind.FIELD){

                VariableElement variableElement = (VariableElement) element;
                System.out.println(3 + variableElement.getSimpleName().toString());
                fields.add(variableElement);
            }

        }
        try {
            generateJava(typeElement, fields);
        } catch (IOException e) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return true;
    }

    private void generateJava(TypeElement elements, List<VariableElement> fields) throws IOException {

        JavaFileObject javaFileObject = mFiler.createSourceFile("ThroughConstant", elements);
        PrintWriter writer = new PrintWriter(javaFileObject.openWriter());

        writer.println("//Automatic generation, please do not modify ");
        writer.println("package com.seasonfif.swiftrouter;");
        writer.println("public class ThroughConstant {");

        for (VariableElement field : fields) {
            String fieldValue;
            if (field.getConstantValue() instanceof String){
                fieldValue = '"'+field.getConstantValue().toString()+'"';
            }else {
                fieldValue = field.getConstantValue().toString();
            }

            writer.println("public static final " + field.asType().toString() + " "
                    + field.getSimpleName().toString()+ "="
                    + fieldValue
                    + ";");
        }

        writer.println("}");

        writer.flush();
        writer.close();
    }
}
