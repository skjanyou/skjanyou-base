package com.skjanyou.start.core;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	private static List<Command> cmdList = new ArrayList<>();
	public interface Command {
		public boolean isMatch( String key );
		public void processCommand();
	}
	public interface CommandProcess {
		public void process();
	}
	
	public static synchronized void regist( Command command ){
		cmdList.add(command);
	}
	
	public static void processCommand( String key ){
		for( Command command : cmdList ){
			if( command.isMatch(key) ){
				command.processCommand();
			}
		}
	}
	
	
	public static class Cmd implements Command {
		private CommandProcess process;
		private String cmdKey;
		public Cmd( String cmdKey,CommandProcess process ){
			this.cmdKey = cmdKey;
			this.process = process;
		}

		@Override
		public boolean isMatch(String key) {
			return this.cmdKey != null && this.cmdKey.equalsIgnoreCase(key);
		}

		@Override
		public void processCommand() {
			if( this.process != null ){
				this.process.process();
			}
		}
	}
}
