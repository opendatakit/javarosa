package org.javarosa.xform.parse;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.condition.EvaluationContext;
import org.javarosa.core.model.instance.TreeReference;
import org.javarosa.xpath.expr.XPathPathExpr;
import org.javarosa.xpath.parser.XPathSyntaxException;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.javarosa.xpath.XPathParseTool.parseXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class XFormParserTest {
    @Test public void parsesSimpleForm() throws IOException {
        FormDef formDef = parse("simple-form.xml");
        assertEquals(formDef.getTitle(), "Simple Form");
    }

    @Test public void parsesForm2() throws IOException {
        FormDef formDef = parse("form2.xml");
        assertEquals("My Survey", formDef.getTitle());
        assertEquals(3, formDef.getChildren().size());
        assertEquals("What is your first name?", formDef.getChild(0).getLabelInnerText());
    }

    @Test public void parsesExternalSecondaryInstanceForm() throws IOException, XPathSyntaxException {
        FormDef formDef = parse("external-secondary-instance.xml");
        assertEquals("Form with external secondary instance", formDef.getTitle());
        TreeReference treeReference = ((XPathPathExpr)
                parseXPath("instance('towns')/data_set")).getReference();
        EvaluationContext evaluationContext = formDef.getEvaluationContext();
        List<TreeReference> treeReferences = evaluationContext.expandReference(treeReference);
        System.out.println(treeReferences);
        assertNotEquals(0, treeReference.size());
    }

    private FormDef parse(String formName) throws IOException {
        XFormParser parser = new XFormParser(new FileReader("resources/" + formName));
        return parser.parse();
    }
}
