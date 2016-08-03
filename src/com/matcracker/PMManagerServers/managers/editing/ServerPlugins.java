package com.matcracker.PMManagerServers.managers.editing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.matcracker.PMManagerServers.API.PluginCreatorAPI;
import com.matcracker.PMManagerServers.API.UtilityServersAPI;
import com.matcracker.PMManagerServers.API.PluginCreatorAPI.CodeStructures;
import com.matcracker.PMManagerServers.lang.BaseLang;
import com.matcracker.PMManagerServers.managers.Manager;
import com.matcracker.PMManagerServers.utility.PMEvents;
import com.matcracker.PMManagerServers.utility.Utility;
import com.matcracker.PMManagerServers.utility.UtilityColor;

import com.matcracker.PMManagerServers.utility.PMEvents.Parameter;

public class ServerPlugins{
	
	public static void pluginsMenu(){
		Utility.cleanScreen();
		System.out.println(Utility.softwareName);
		System.out.println(Utility.setTitle(UtilityColor.RED, "Server Plugins"));
		System.out.println("1- " + "Get list of plugins on your server.");
		System.out.println("2- " + "Delete a plugin.");
		System.out.println("3- " + "Create a plugin.");
		System.out.println("4- " + BaseLang.translate("pm.standard.back"));
		
		int opt = Utility.readInt(BaseLang.translate("pm.choice.option") + " ", null);
		
		if(opt == 1)
			getPluginsList();
		
		if(opt == 2)
			deletePlugin();
		
		if(opt == 3)
			createPlugin();
		
		if(opt == 4)
			Manager.managerMenu();
		pluginsMenu();
	}
	
	private static void createPlugin() {
		PluginCreatorAPI plcr = new PluginCreatorAPI();
		Utility.cleanScreen();
		System.out.println(Utility.setTitle(UtilityColor.YELLOW, "Create plugin"));
		System.out.println("1- " + "Create plugin.yml");
		System.out.println("2- " + "Create plugin structure");
		int opt = Utility.readInt(BaseLang.translate("pm.choice.option") + " ", "[Don't shut down the software when you are creating a plugin! You can lose all your progress]");
		
		if(opt == 1){
			plcr.getPluginYAML();
			plcr.createPluginYAML();
			Utility.waitConfirm(UtilityColor.GREEN + "File plugin.yml created!");
		}
		
		if(opt == 2){
			boolean finish = false;
			do{
				Utility.cleanScreen();
				System.out.println(Utility.setTitle(UtilityColor.YELLOW, "Create structure"));
				System.out.println("1- Add standard structure (onEnable/onDisable)");
				System.out.println("2- Add events structure");
				System.out.println("3- Add commands structure");
				System.out.println("4- Save and finish");
				System.out.println("5- " + BaseLang.translate("pm.standard.back"));
				int struct = Utility.readInt("Select structure type: ", "[Recommend, start from step 1]");
				
				if(struct == 5)
					break;
				
				if(struct == 1)
					plcr.setClassStructure();
					
				if(struct == 2){
					PMEvents pmev = plcr.getEvents();
					plcr.setListener(true);
					
					Utility.cleanScreen();
					System.out.println(Utility.setTitle(UtilityColor.YELLOW, "Events Selector"));
					System.out.println("1- Blocks");
					System.out.println("2- Entities");
					System.out.println("3- Inventories");
					System.out.println("4- Levels");
					System.out.println("5- Players");
					System.out.println("6- Plugin");
					System.out.println("7- " + BaseLang.translate("pm.standard.back"));
					int event = Utility.readInt("Select event to add: ", null);
					
					if(event == 7) return; 
					String[] events = new String[pmev.entityEvents.length];
					
					if(event == 1)
						events = pmev.blockEvents;
					
					if(event == 2)
						events = pmev.entityEvents;
					
					if(event == 3)
						events = pmev.inventoryEvents;
					
					if(event == 4)
						events = pmev.levelEvents;
					
					if(event == 5)
						events = pmev.playerEvents;
					
					if(event == 6)
						events = pmev.pluginEvents;
					
					int i = 0;
					for(i = 0; i < events.length; i++)
						System.out.printf("%d) %s\n", (i+1), pmev.getEvent(i, events));
				
					System.out.println((i+1) + ") " + BaseLang.translate("pm.standard.back"));

					int type = Utility.readInt("Select type: ", null);
					
					if(type == (i+1) || type > events.length) return;
					
					if(type > 0){
						do{
							i = 0;
							Utility.cleanScreen();
							plcr.addEvent(pmev.getEvent(type-1, events));
							System.out.println(Utility.setTitle(UtilityColor.YELLOW, "Parameter Selector"));
							System.out.println(UtilityColor.BLUE + "---------------------------------");
							System.out.println(UtilityColor.PURPLE + "Current code: ");
							if(!plcr.events.isEmpty()) System.out.println(plcr.getEventsContent());
							System.out.println(plcr.getEventContent());
							System.out.println(UtilityColor.FORMAT_RESET + UtilityColor.BLUE + "---------------------------------" + UtilityColor.WHITE);
							
							boolean[] accepted = pmev.filterParam(pmev.getEvent(type-1, events));
							for(i = 0; i < accepted.length; i++){
								if(accepted[i])
									System.out.printf("%d) %s\n", (i+1), Parameter.values()[i].getName());
							}
							System.out.println((i+1) + ") " + "Add custom line code");
							System.err.println((i+2) + ") " + "Cancel code");
							System.out.println((i+3) + ") " + "Add code structure (If, for, while...)");
							System.out.println((i+4) + ") " + "Save and leave event editor");
							
							int param = Utility.readInt("Select parameter: ", null);
							
							if(param <= 0) return;
							
							if(param == (i+2)) plcr.removeContext();
							
							if(param == (i+3)){
								for(int c = 0; c < CodeStructures.values().length; c++)
									System.out.println((c+1) + ") " + CodeStructures.values()[c].toString());
								
								int code = Utility.readInt("Select structure code type: ", null);
								plcr.addContext(param-1, true, plcr.getStructure(plcr.toCodeStructure(code-1)));
							}
							
							if(param == (i+4)) plcr.setEventSetted(true);
							
							if(!plcr.isEventSetted()){
								if(param <= (i+1)){
									//Custom
									if(param == (i+1)){
											String custom = Utility.readString("Write the code to insert:\n" , "[Use escapes char]");
											plcr.addContext(param-1, true, custom);
										
									}else{
										if(accepted[param-1]){
											if(param != 9){
												String vb = Utility.readString("Select name of variable: ", "[Example: $player, $block, ect...]");
												plcr.setVariableName(vb);
											}
											plcr.addContext(param-1, false, Parameter.values()[param-1].getName());
										}
									}
								}
							}
							
							if(param == (i+4)){
								plcr.saveEvent();
								Utility.waitConfirm(UtilityColor.GREEN + "Event added correctly!");
							}
								
						}while(!plcr.isEventSetted());
						plcr.setEventSetted(false);
					}							
				}
				
				
				if(struct == 4){
					plcr.createNewClass();
					finish = true;
					Utility.waitConfirm(UtilityColor.GREEN + "Class created!");
				}
			}while(!finish);
		}
		pluginsMenu();
	}

	private static void deletePlugin(){
		Utility.showServers();
		int server = Utility.readInt(BaseLang.translate("pm.choice.server") + " ", null);
		
		if(server >= 1 && server <= UtilityServersAPI.getNumberServers()){
			if(UtilityServersAPI.checkServersFile("Path", "path_", server)){
				String path = UtilityServersAPI.getPath(server);
				if(path != null){
					File pluginsPath = new File(path + "plugins" + File.separator);
					List<File> files = new ArrayList<>();
					int i = 1;
					boolean found = false;
					for(File file : pluginsPath.listFiles()){
						if(file.isFile() && file.getName().endsWith(".phar")){
							found = true;
							System.out.println(i + ") " + file.getName());
							files.add(file);
						}
					}

					if(found){
						int opt = 0;
						do{
							opt = Utility.readInt("Select plugin to delete: ", null);
							try{
								File plug = files.get(opt-1);
								files.get(opt-1).delete();
								Utility.waitConfirm(UtilityColor.GREEN + plug.getName() + " deleted successfully!");
							}catch(IndexOutOfBoundsException e){
								Utility.waitConfirm(UtilityColor.RED + "Error on deleting plugin file!");
							}
						}while(opt < i || opt > i);
					}else
						Utility.waitConfirm(UtilityColor.RED + "Plugins don't found!");
					
				}else
					Utility.waitConfirm(BaseLang.translate("pm.errors.pathNull"));
			}else
				Utility.waitConfirm(BaseLang.translate("pm.errors.pathNotFound"));
		}
		pluginsMenu();
	}

	private static void getPluginsList(){
		Utility.showServers();
		int server = Utility.readInt(BaseLang.translate("pm.choice.server") + " ", null);
		
		if(server >= 1 && server <= UtilityServersAPI.getNumberServers()){
			if(UtilityServersAPI.checkServersFile("Path", "path_", server)){
				String path = UtilityServersAPI.getPath(server);
				if(path != null){
					path = path + "plugins/";
					File files = new File(path);
					int i = 1;
					for(File file : files.listFiles())
						if(file.isFile() && file.getName().endsWith(".phar"))
							System.out.println(i + ") " + file.getName());
					Utility.waitConfirm(BaseLang.translate("pm.standard.enter"));
				}else
					Utility.waitConfirm(BaseLang.translate("pm.errors.pathNull"));
			}else
				Utility.waitConfirm(BaseLang.translate("pm.errors.pathNotFound"));
		}
		pluginsMenu();
	}
}


