/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2020 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.test.formulaeditor;

import android.content.Context;

import org.catrobat.catroid.formulaeditor.Functions;
import org.catrobat.catroid.formulaeditor.InternFormula;
import org.catrobat.catroid.formulaeditor.InternToken;
import org.catrobat.catroid.formulaeditor.InternTokenType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;

import static junit.framework.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class InternFormulaRegexAssistantTest {

	private InternFormula internFormula;

	@Test //Regex Funktion ist ausgewählt - expected: false (nichts einfügen)
	public void testRegexFunctionIsSelected() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf(Functions.REGEX.name()) + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("Regular expression is selected and therefore we are already in a regular "
						+ "expression",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Regex Funktion 1st Param ausgewählt - expected: false
	public void testRegexFunctionFirstParamIsSelected() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf("Hello") + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("First parameter of regular expression is selected and therefore we are "
						+ "already in a regular expression",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Regex Funktion 2nd Param ausgewählt - expected: false
	public void testRegexFunctionSecondParamIsSelected() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf("World") + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("Second parameter of regular expression is selected, therefore we are "
						+ "already in a regular expression",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Join ausgewählt - kein äußeres Regex - expected: true
	public void testJoinFunctionIsSelectedWithNoOutsideRegexFunction() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.JOIN.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf(Functions.JOIN.name()) + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertTrue("Join is selected and no regular expression is outside, therefore a regular "
						+ "expression should be inserted",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Join ausgewählt - äußeres Regex existiert - expected: false
	public void testJoinFunctionIsSelectedWithOutsideRegexFunction() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		//regular expression(join('Hello','World'), 'Foobar')
		internTokens.add((new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name())));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.JOIN.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "Foobar"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf(Functions.JOIN.name()) + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("Join is selected and regular expression is outside, therefore a regular "
						+ "expression should not be inserted",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Join 1st param nicht regex ausgewählt - expected: true
	public void testJoinFunctionFirstParamIsSelectedAndIsNoRegexFunction() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.JOIN.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf("Hello") + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertTrue("First param of Join function is selected and is not a regular expression, "
						+ "therefore a regular expression should be inserted",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Join 2nd param nicht regex ausgewählt - expected: true
	public void testJoinFunctionSecondParamIsSelectedAndIsNoRegexFunction() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.JOIN.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf("World") + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertTrue("Second param of Join function is selected and is not a regular expression, "
						+ "therefore a regular expression should be inserted",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Join 1st param regex ausgewählt - expected: false
	public void testJoinFunctionFirstParamIsSelectedAndIsRegexFunction() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		//join(regular expression('Hello', 'World'), 'Foobar')
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.JOIN.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "Foobar"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf(Functions.REGEX.name()) + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("Regular expression function in Join function is selected, therefore no "
						+ "regular expression should be inserted",
				internFormula.isSelectionInsideRegularExpression());
	}

	@Test //Join 2nd param regex ausgewählt - expected: false
	public void testJoinFunctionSecondParamIsSelectedAndIsRegexFunction() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		//join('Foobar', regular expression('Hello', 'World'))
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.JOIN.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Foobar"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf(Functions.REGEX.name()) + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("Regular expression function in Join function is selected, therefore no "
						+ "regular expression should be inserted",
				internFormula.isSelectionInsideRegularExpression());
	}






	//2nd Regex ausgewählt - wählt 1st param in 2nd regex aus
}
