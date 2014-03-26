package de.eightbitboy.roo.envers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.lang.model.element.AnnotationValue;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.operations.AbstractOperations;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.DependencyScope;
import org.springframework.roo.project.DependencyType;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Implementation of operations this add-on offers.
 *
 * @since 1.1
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class EnversOperationsImpl extends AbstractOperations implements EnversOperations {
	private static final JavaSymbolName VALUE = new JavaSymbolName("value");
	
	private static Logger LOG = HandlerUtils.getLogger(EnversCommands.class);
    
    /**
     * Use ProjectOperations to install new dependencies, plugins, properties, etc into the project configuration
     */
    @Reference private ProjectOperations projectOperations;
    
    /**
     * Use PathResolver to get paths for copying files
     */
    @Reference private PathResolver pathResolver;

    /**
     * Use TypeLocationService to find types which are annotated with a given annotation in the project
     */
    @Reference private TypeLocationService typeLocationService;
    
    /**
     * Use TypeManagementService to change types
     */
    @Reference private TypeManagementService typeManagementService;
    
    @Reference private MetadataService metadataService;

    /** {@inheritDoc} */
    public boolean isCommandAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }

    /** {@inheritDoc} */
    public void annotateType(final JavaType type, final JavaPackage javaPackage) {
    	//TODO check for existing controller
    	
        // Use Roo's Assert type for null checks
        Validate.notNull(type, "Java type required");
        Validate.notNull(javaPackage, "Java package required, web mvc all command package required");

        // Obtain ClassOrInterfaceTypeDetails for this java type
        ClassOrInterfaceTypeDetails entityDetails = typeLocationService.getTypeDetails(type);

        // Test if the annotation already exists on the target type
        if (entityDetails != null && MemberFindingUtils.getAnnotationOfType(entityDetails.getAnnotations(), new JavaType(RooEnvers.class.getName())) == null) {
            ClassOrInterfaceTypeDetailsBuilder classOrInterfaceTypeDetailsBuilder = new ClassOrInterfaceTypeDetailsBuilder(entityDetails);
            
            // Create JavaType instance for the add-ons trigger annotation
            JavaType rooEnvers = new JavaType(RooEnvers.class.getName());

            // Create Annotation metadata
            AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(rooEnvers);
            
            // Add annotation to target type
            classOrInterfaceTypeDetailsBuilder.addAnnotation(annotationBuilder.build());
            
            // Save changes to disk
            typeManagementService.createOrUpdateTypeOnDisk(classOrInterfaceTypeDetailsBuilder.build());
        }
        
        
        // Get details for existing controller
        JavaType typeController = new JavaType(javaPackage.getFullyQualifiedPackageName() + "." + type.getSimpleTypeName() + "Controller");
        ClassOrInterfaceTypeDetails typeControllerDetails = typeLocationService.getTypeDetails(typeController);
        
        // Add annotation @RooEnversController to existing controller
        ClassOrInterfaceTypeDetailsBuilder classOrInterfaceTypeDetailsBuilder = new ClassOrInterfaceTypeDetailsBuilder(typeControllerDetails);
        JavaType rooEnversController = new JavaType("de.eightbitboy.roo.envers.controller.RooEnversController");
        
        
        final List<AnnotationAttributeValue<?>> rooEnversControllerAttributes = new ArrayList<AnnotationAttributeValue<?>>();
        rooEnversControllerAttributes.add(new StringAttributeValue(new JavaSymbolName("path"), type.getSimpleTypeName().toLowerCase()));
        
        AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(rooEnversController, rooEnversControllerAttributes);        
        classOrInterfaceTypeDetailsBuilder.addAnnotation(annotationBuilder.build());
        typeManagementService.createOrUpdateTypeOnDisk(classOrInterfaceTypeDetailsBuilder.build());
    }
    
    /** {@inheritDoc} */
    public void setup() {
        // Install the add-on Google code repository needed to get the annotation 
        //projectOperations.addRepository("", new Repository("Envers Roo add-on repository", "Envers Roo add-on repository", "https://roo-envers.googlecode.com/svn/repo"));
        
        List<Dependency> dependencies = new ArrayList<Dependency>();
        
        // Install the dependency on the add-on jar
        //TODO move this to configuration.xml ?
        dependencies.add(new Dependency("de.eightbitboy.roo.envers", "de.eightbitboy.roo.envers", "0.1.0", DependencyType.JAR, DependencyScope.PROVIDED));
        
        // Install dependencies defined in external XML file
        for (Element dependencyElement : XmlUtils.findElements("/configuration/batch/dependencies/dependency", XmlUtils.getConfiguration(getClass()))) {
            dependencies.add(new Dependency(dependencyElement));
        }

        // Add all new dependencies to pom.xml
        projectOperations.addDependencies("", dependencies);
        
        // Modify tags to create views for entity revisions
        this.modifyTags(); //TODO use modified tags for audited entities only
    }
    
    private void modifyTags(){
    	//TODO modify tags instead of overwriting them!!!
    	String targetPath = pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/tags/form/fields");
    	
    	copyDirectoryContents("tags/*.*", targetPath, true);
    }
}