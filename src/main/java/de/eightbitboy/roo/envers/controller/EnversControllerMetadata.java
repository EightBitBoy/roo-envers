package de.eightbitboy.roo.envers.controller;

import static org.springframework.roo.model.JavaType.STRING;
import static org.springframework.roo.model.SpringJavaType.MODEL;
import static org.springframework.roo.model.SpringJavaType.PATH_VARIABLE;
import static org.springframework.roo.model.SpringJavaType.REQUEST_MAPPING;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.roo.addon.web.mvc.controller.details.JavaTypePersistenceMetadataDetails;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.support.logging.HandlerUtils;

public class EnversControllerMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
	
	private static Logger LOG = HandlerUtils.getLogger(EnversControllerMetadata.class);
	
    private static final String PROVIDES_TYPE_STRING = EnversControllerMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private static final StringAttributeValue PRODUCES_HTML = new StringAttributeValue(
            new JavaSymbolName("produces"), "text/html");
    
    private EnversControllerAnnotationValues annotationValues;

    private JavaType type;
    private String typeName;
    private String typeNamePlural;
    
    public static final String getMetadataIdentiferType() {
        return PROVIDES_TYPE;
    }
    
    public static final String createIdentifier(JavaType javaType, LogicalPath path) {
        return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
    }
    
    public static final JavaType getJavaType(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static final LogicalPath getPath(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }
	
	protected EnversControllerMetadata(EnversControllerAnnotationValues annotationValues, String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
		super(identifier, aspectName, governorPhysicalTypeMetadata);
		LOG.info("Adding EnversController code");
		
		this.annotationValues = annotationValues;
		
		this.type = this.annotationValues.getType();
		this.typeName = this.type.getSimpleTypeName();
		this.typeNamePlural = this.typeName + "s";
		
		builder.addMethod(getListAuditsMethod());
		
		itdTypeDetails = builder.build();
	}
	
    private MethodMetadata getListAuditsMethod() {
    	final JavaSymbolName methodName = new JavaSymbolName("listAudits");
        if (governorHasMethodWithSameName(methodName)) {
            return null;
        }
        
        //make sure to import the class of the entity
        builder.getImportRegistrationResolver().addImport(this.type);

        final List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
        attributes.add(new StringAttributeValue(new JavaSymbolName("value"), "/audits/{id}"));
        final AnnotationMetadataBuilder pathVariableAnnotation = new AnnotationMetadataBuilder(PATH_VARIABLE, attributes);

        final List<AnnotatedJavaType> parameterTypes = Arrays.asList(
                new AnnotatedJavaType(pathVariableAnnotation.build().getAnnotationType()),
                new AnnotatedJavaType(MODEL));
        
        final List<JavaSymbolName> parameterNames = Arrays.asList(
                new JavaSymbolName("id"),
                new JavaSymbolName("uiModel"));

        final List<AnnotationAttributeValue<?>> requestMappingAttributes = new ArrayList<AnnotationAttributeValue<?>>();
        requestMappingAttributes.add(new StringAttributeValue(new JavaSymbolName("value"), "/{id}"));
        requestMappingAttributes.add(PRODUCES_HTML);
        final AnnotationMetadataBuilder requestMapping = new AnnotationMetadataBuilder(
                REQUEST_MAPPING, requestMappingAttributes);
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        annotations.add(requestMapping);

        final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();

        bodyBuilder.appendFormalLine("uiModel.addAttribute(\""
                + this.typeName.toLowerCase() + "\", "
                + this.typeName + ".find" + this.typeName + "(id)" + ");");
        bodyBuilder.appendFormalLine("return \"" + this.typeNamePlural.toLowerCase() + "/show\";");

        final MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(
                getId(), Modifier.PUBLIC, methodName, STRING, parameterTypes,
                parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        return methodBuilder.build();
    }
}