package org.eclipse.swtbot.swt.finder.keyboard;

import static java.awt.event.InputEvent.SHIFT_MASK;
import static java.awt.event.KeyEvent.*;
import static org.eclipse.swtbot.swt.finder.keyboard.KeyStrokeMapping.mapping;
import static org.eclipse.swtbot.swt.finder.keyboard.KeyStrokeMapping.Modifier.NO_MASK;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeyboardLayout_en implements KeyboardLayout {

	private static final Collection<KeyStrokeMapping>	mappings	= Collections.unmodifiableCollection(createMappings());

	public Collection<KeyStrokeMapping> layout() {
		return mappings;
	}

	private static Collection<KeyStrokeMapping> createMappings() {
		Set<KeyStrokeMapping> mappings = new HashSet<KeyStrokeMapping>();
		mappings.addAll(KeyStrokeMappings.createMappings());

		// numbers 0-9
		mappings.add(mapping('0', VK_0, NO_MASK));
		mappings.add(mapping('1', VK_1, NO_MASK));
		mappings.add(mapping('2', VK_2, NO_MASK));
		mappings.add(mapping('3', VK_3, NO_MASK));
		mappings.add(mapping('4', VK_4, NO_MASK));
		mappings.add(mapping('5', VK_5, NO_MASK));
		mappings.add(mapping('6', VK_6, NO_MASK));
		mappings.add(mapping('7', VK_7, NO_MASK));
		mappings.add(mapping('8', VK_8, NO_MASK));
		mappings.add(mapping('9', VK_9, NO_MASK));

		// symbols corresponding to 0-9
		mappings.add(mapping(')', VK_0, SHIFT_MASK));
		mappings.add(mapping('!', VK_1, SHIFT_MASK));
		mappings.add(mapping('@', VK_2, SHIFT_MASK));
		mappings.add(mapping('#', VK_3, SHIFT_MASK));
		mappings.add(mapping('$', VK_4, SHIFT_MASK));
		mappings.add(mapping('%', VK_5, SHIFT_MASK));
		mappings.add(mapping('^', VK_6, SHIFT_MASK));
		mappings.add(mapping('&', VK_7, SHIFT_MASK));
		mappings.add(mapping('*', VK_8, SHIFT_MASK));
		mappings.add(mapping('(', VK_9, SHIFT_MASK));

		// a-z small
		mappings.add(mapping('a', VK_A, NO_MASK));
		mappings.add(mapping('b', VK_B, NO_MASK));
		mappings.add(mapping('c', VK_C, NO_MASK));
		mappings.add(mapping('d', VK_D, NO_MASK));
		mappings.add(mapping('e', VK_E, NO_MASK));
		mappings.add(mapping('f', VK_F, NO_MASK));
		mappings.add(mapping('g', VK_G, NO_MASK));
		mappings.add(mapping('h', VK_H, NO_MASK));
		mappings.add(mapping('i', VK_I, NO_MASK));
		mappings.add(mapping('j', VK_J, NO_MASK));
		mappings.add(mapping('k', VK_K, NO_MASK));
		mappings.add(mapping('l', VK_L, NO_MASK));
		mappings.add(mapping('m', VK_M, NO_MASK));
		mappings.add(mapping('n', VK_N, NO_MASK));
		mappings.add(mapping('o', VK_O, NO_MASK));
		mappings.add(mapping('p', VK_P, NO_MASK));
		mappings.add(mapping('q', VK_Q, NO_MASK));
		mappings.add(mapping('r', VK_R, NO_MASK));
		mappings.add(mapping('s', VK_S, NO_MASK));
		mappings.add(mapping('t', VK_T, NO_MASK));
		mappings.add(mapping('u', VK_U, NO_MASK));
		mappings.add(mapping('v', VK_V, NO_MASK));
		mappings.add(mapping('w', VK_W, NO_MASK));
		mappings.add(mapping('x', VK_X, NO_MASK));
		mappings.add(mapping('y', VK_Y, NO_MASK));
		mappings.add(mapping('z', VK_Z, NO_MASK));

		// a-z capital
		mappings.add(mapping('A', VK_A, SHIFT_MASK));
		mappings.add(mapping('B', VK_B, SHIFT_MASK));
		mappings.add(mapping('C', VK_C, SHIFT_MASK));
		mappings.add(mapping('D', VK_D, SHIFT_MASK));
		mappings.add(mapping('E', VK_E, SHIFT_MASK));
		mappings.add(mapping('F', VK_F, SHIFT_MASK));
		mappings.add(mapping('G', VK_G, SHIFT_MASK));
		mappings.add(mapping('H', VK_H, SHIFT_MASK));
		mappings.add(mapping('I', VK_I, SHIFT_MASK));
		mappings.add(mapping('J', VK_J, SHIFT_MASK));
		mappings.add(mapping('K', VK_K, SHIFT_MASK));
		mappings.add(mapping('L', VK_L, SHIFT_MASK));
		mappings.add(mapping('M', VK_M, SHIFT_MASK));
		mappings.add(mapping('N', VK_N, SHIFT_MASK));
		mappings.add(mapping('O', VK_O, SHIFT_MASK));
		mappings.add(mapping('P', VK_P, SHIFT_MASK));
		mappings.add(mapping('Q', VK_Q, SHIFT_MASK));
		mappings.add(mapping('R', VK_R, SHIFT_MASK));
		mappings.add(mapping('S', VK_S, SHIFT_MASK));
		mappings.add(mapping('T', VK_T, SHIFT_MASK));
		mappings.add(mapping('U', VK_U, SHIFT_MASK));
		mappings.add(mapping('V', VK_V, SHIFT_MASK));
		mappings.add(mapping('W', VK_W, SHIFT_MASK));
		mappings.add(mapping('X', VK_X, SHIFT_MASK));
		mappings.add(mapping('Y', VK_Y, SHIFT_MASK));
		mappings.add(mapping('Z', VK_Z, SHIFT_MASK));

		// special characters
		mappings.add(mapping('`', VK_BACK_QUOTE, NO_MASK));
		mappings.add(mapping('-', VK_MINUS, NO_MASK));
		mappings.add(mapping('=', VK_EQUALS, NO_MASK));

		mappings.add(mapping('[', VK_OPEN_BRACKET, NO_MASK));
		mappings.add(mapping(']', VK_CLOSE_BRACKET, NO_MASK));
		mappings.add(mapping('\\', VK_BACK_SLASH, NO_MASK));

		mappings.add(mapping(';', VK_SEMICOLON, NO_MASK));
		mappings.add(mapping('\'', VK_QUOTE, NO_MASK));

		mappings.add(mapping(',', VK_COMMA, NO_MASK));
		mappings.add(mapping('.', VK_PERIOD, NO_MASK));
		mappings.add(mapping('/', VK_SLASH, NO_MASK));

		// special characters (with SHIFT key)
		mappings.add(mapping('~', VK_BACK_QUOTE, SHIFT_MASK));
		mappings.add(mapping('_', VK_MINUS, SHIFT_MASK));
		mappings.add(mapping('+', VK_EQUALS, SHIFT_MASK));

		mappings.add(mapping('{', VK_OPEN_BRACKET, SHIFT_MASK));
		mappings.add(mapping('}', VK_CLOSE_BRACKET, SHIFT_MASK));
		mappings.add(mapping('|', VK_BACK_SLASH, SHIFT_MASK));

		mappings.add(mapping(':', VK_SEMICOLON, SHIFT_MASK));
		mappings.add(mapping('"', VK_QUOTE, SHIFT_MASK));

		mappings.add(mapping('<', VK_COMMA, SHIFT_MASK));
		mappings.add(mapping('>', VK_PERIOD, SHIFT_MASK));
		mappings.add(mapping('?', VK_SLASH, SHIFT_MASK));

		return mappings;
	}
}
