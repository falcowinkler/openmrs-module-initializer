package org.openmrs.module.initializer.api.conceptsources;

import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.initializer.api.BaseLineProcessor;
import org.openmrs.module.initializer.api.CsvLine;
import org.openmrs.module.initializer.api.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConceptSourceCsvParser extends CsvParser<ConceptSource, BaseLineProcessor<ConceptSource>> {
	
	private final ConceptService conceptService;
	
	private final BaseLineProcessor<ConceptSource> lineProcessor;
	
	/**
	 * Most CSV parsers are built on a single line processor. This superclass constructor should be used
	 * to initialize such parsers.
	 * 
	 * @param lineProcessor The single line processor for the CSV parser.
	 */
	@Autowired
	protected ConceptSourceCsvParser(@Qualifier("conceptService") ConceptService conceptService,
	    ConceptSourceLineProcessor lineProcessor) {
		super(lineProcessor);
		this.conceptService = conceptService;
		this.lineProcessor = lineProcessor;
	}
	
	@Override
	public ConceptSource bootstrap(CsvLine line) throws IllegalArgumentException {
		ConceptSource result = new ConceptSource();
		lineProcessor.fill(result, line);
		return result;
	}
	
	@Override
	public ConceptSource save(ConceptSource instance) {
		return conceptService.saveConceptSource(instance);
	}
	
	@Override
	public Domain getDomain() {
		return Domain.CONCEPT_SOURCES;
	}
}
