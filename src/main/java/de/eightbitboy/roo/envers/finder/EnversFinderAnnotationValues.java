package de.eightbitboy.roo.envers.finder;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;

public class EnversFinderAnnotationValues extends AbstractAnnotationValues {

	@AutoPopulate String path;
	
	static final JavaType ROO_ENVERS_FINDER = new JavaType("de.eightbitboy.roo.envers.finder.RooEnversFinder");
	
	protected EnversFinderAnnotationValues(final ClassOrInterfaceTypeDetails governorPhysicalTypeDetails) {
		super(governorPhysicalTypeDetails, ROO_ENVERS_FINDER);
		AutoPopulationUtils.populate(this, annotationMetadata);
	}
	
	protected EnversFinderAnnotationValues(final PhysicalTypeMetadata governorPhysicalTypeMetadata) {
		super(governorPhysicalTypeMetadata, ROO_ENVERS_FINDER);
		AutoPopulationUtils.populate(this, annotationMetadata);
	}
	
	public String getPath(){
		return path;
	}
}