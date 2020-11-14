package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;


class InsbecdrConsoleCommandTests {
	@BeforeEach
	public void setup() {
	}


	@Test
	public void empty_command() {
		InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("");

		assertTrue(command.isEmpty());
		assertEquals(null, command.getCommand());
		assertEquals(null, command.getArgument());
	}


	@Test
	public void null_command() {
		InsbecdrConsoleCommand command = new InsbecdrConsoleCommand(null);

		assertTrue(command.isEmpty());
		assertEquals(null, command.getCommand());
		assertEquals(null, command.getArgument());
	}


	@Test
	public void command_without_parameter() {
		InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("h");

		assertFalse(command.isEmpty());
		assertEquals("h", command.getCommand());
		assertEquals(null, command.getArgument());
	}


	@Test
	public void command_with_parameter() {
		InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("v 000011");

		assertFalse(command.isEmpty());
		assertEquals("v", command.getCommand());
		assertEquals("000011", command.getArgument());
	}


	@Test
	public void command_with_multiple_parameter_result_in_one_parameter() {
		InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("f 100 101 102");

		assertFalse(command.isEmpty());
		assertEquals("f", command.getCommand());
		assertEquals("100 101 102", command.getArgument());
	}
}
