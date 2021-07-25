package dz.jtsgen.processor.jtp.conv;

import dz.jtsgen.processor.jtp.info.TSProcessingInfo;

public class PreserveExecutablesTypeScriptAnnotationProcessor extends TypeScriptAnnotationProcessor {

  public PreserveExecutablesTypeScriptAnnotationProcessor(TSProcessingInfo processingInfo) {
    super(processingInfo, new PreserveExecutablesJavaTypeConverter(processingInfo));
  }
}