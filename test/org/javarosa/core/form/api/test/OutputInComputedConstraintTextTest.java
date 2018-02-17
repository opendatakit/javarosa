package org.javarosa.core.form.api.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.javarosa.core.PathConst;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.LongData;
import org.javarosa.core.services.PrototypeManager;
import org.javarosa.core.test.FormParseInit;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.form.api.FormEntryPrompt;
import org.junit.Before;
import org.junit.Test;

public class OutputInComputedConstraintTextTest {
  static {
    PrototypeManager.registerPrototype("org.javarosa.model.xform.XPathReference");
  }

  private Map<String, FormIndex> formIndexesById = new HashMap<>();
  private FormDef formDef;
  private FormEntryController ctrl;
  private FormEntryModel model;

  @Before
  public void setUp() {
    FormParseInit fpi = new FormParseInit();
    fpi.setFormToParse((new File(PathConst.getTestResourcePath(), "constraint-message-error.xml")).getAbsolutePath());
    formDef = fpi.getFormDef();
    formDef.getLocalizer().setLocale("English");
    ctrl = fpi.getFormEntryController();
    model = fpi.getFormEntryModel();
    buildIndexes();
  }

  @Test
  public void testComputedQuestionText() {
    // Answer first question to check label and constraint texts on next questions
    ctrl.answerQuestion(getFormIndex("/constraintMessageError/village:label"), new LongData(1), true);

    assertEquals(
        "Please only conduct this survey with children aged 6 TO 24 MONTHS.",
        getFormEntryPrompt("/constraintMessageError/ageSetNote:label").getQuestionText()
    );
  }

  @Test
  public void testComputedConstraintText() {
    // Answer first question to check label and constraint texts on next questions
    ctrl.answerQuestion(getFormIndex("/constraintMessageError/village:label"), new LongData(1), true);

    assertEquals(
        "Please only conduct this survey with children aged 6 TO 24 MONTHS.",
        getFormEntryPrompt("/constraintMessageError/age:label").getConstraintText()
    );
  }

  private void buildIndexes() {
    ctrl.jumpToIndex(FormIndex.createBeginningOfFormIndex());
    do {
      FormEntryCaption fep = model.getCaptionPrompt();
      IFormElement formElement = fep.getFormElement();
      if (formElement instanceof QuestionDef)
        formIndexesById.put(formElement.getTextID(), fep.getIndex());
    } while (ctrl.stepToNextEvent() != FormEntryController.EVENT_END_OF_FORM);
  }

  private FormIndex getFormIndex(String id) {
    if (formIndexesById.containsKey(id))
      return formIndexesById.get(id);
    throw new RuntimeException("FormIndex with id \"" + id + "\" not found");
  }

  private FormEntryPrompt getFormEntryPrompt(String id) {
    return new FormEntryPrompt(formDef, getFormIndex(id));
  }

}