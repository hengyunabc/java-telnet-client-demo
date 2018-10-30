package io.github.hengyunabc.tomcat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetOptionHandler;
import org.apache.commons.net.telnet.WindowSizeOptionHandler;

import jline.Terminal;
import jline.UnixTerminal;
import jline.console.ConsoleReader;
import jline.console.KeyMap;

/**
 *
 * @author hengyunabc 2018-10-29
 *
 */
public class TelnetClientDemo {
	private static final byte CTRL_C = 0x03;

	public static void main(String[] args) throws IOException {
		final ConsoleReader console = new ConsoleReader(System.in, System.out);
		console.setHandleUserInterrupt(true);
		Terminal terminal = console.getTerminal();
		// support catch ctrl+c event
		terminal.disableInterruptCharacter();
		if (terminal instanceof UnixTerminal) {
			((UnixTerminal) terminal).disableLitteralNextCharacter();
		}

		final TelnetClient telnet = new TelnetClient();
		telnet.setConnectTimeout(5000);

		// hack for windows
		int width = terminal.getWidth();
		if (OSUtils.isWindowsOS()) {
			width--;
		}
		// send init terminal size
		TelnetOptionHandler sizeOpt = new WindowSizeOptionHandler(width, terminal.getHeight(), true, true, false,
				false);
		try {
			telnet.addOptionHandler(sizeOpt);
		} catch (InvalidTelnetOptionException e) {
			// ignore
		}

		// ctrl + c event callback
		console.getKeys().bind(new Character((char) CTRL_C).toString(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					console.getCursorBuffer().clear(); // clear current line
					telnet.getOutputStream().write(CTRL_C);
					telnet.getOutputStream().flush();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});

		// ctrl + d event call back
		console.getKeys().bind(new Character(KeyMap.CTRL_D).toString(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		String ip = "127.0.0.1";
		int port = 3658;
		if (args.length == 1) {
			ip = args[0];
		} else if (args.length >= 2) {
			ip = args[0];
			port = Integer.parseInt(args[1]);
		}
		try {
			telnet.connect(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		IOUtil.readWrite(telnet.getInputStream(), telnet.getOutputStream(), console.getInput(), console.getOutput());
	}

}
