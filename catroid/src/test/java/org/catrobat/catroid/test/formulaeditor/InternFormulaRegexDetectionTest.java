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

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

@RunWith(JUnit4.class)
public class InternFormulaRegexDetectionTest {

	private InternFormula internFormula;

	@Test
	public void testEmptyFormula() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = 0;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("EmptyList should not be inside a regular expression",
				internFormula.isSelectedTokenFirstParamOfRegularExpression());
	}

	@Test
	public void testFormulaWithoutRegex() {
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

		assertFalse("Formula without a regular expression cannot have a first regex param",
				internFormula.isSelectedTokenFirstParamOfRegularExpression());
	}

	@Test
	public void testFormulaWithNoBracketInFrontOfSelection() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf("Hello") + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("If there is not bracket in front then this cant be the first param",
				internFormula.isSelectedTokenFirstParamOfRegularExpression());
	}

	@Test
	public void testRegexFormulaWithSelectedFirstParamNotString() {
		ArrayList<InternToken> internTokens = new ArrayList<>();
		internTokens.add(new InternToken(InternTokenType.FUNCTION_NAME, Functions.REGEX.name()));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		internTokens.add(new InternToken(InternTokenType.USER_VARIABLE, "Hello"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		internTokens.add(new InternToken(InternTokenType.STRING, "World"));
		internTokens.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		internFormula = new InternFormula(internTokens);
		internFormula.generateExternFormulaStringAndInternExternMapping(Mockito.mock(Context.class));

		int doubleClickIndex = internFormula.getExternFormulaString().indexOf("Hello") + 1;
		internFormula.setCursorAndSelection(doubleClickIndex, true);

		assertFalse("If there is not bracket in front then this cant be the first param",
				internFormula.isSelectedTokenFirstParamOfRegularExpression());
	}

	@Test
	public void testCorrectRegexWithFirstParamSelected() {
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

		assertTrue("First param in regex should be found",
				internFormula.isSelectedTokenFirstParamOfRegularExpression());
	}
}
