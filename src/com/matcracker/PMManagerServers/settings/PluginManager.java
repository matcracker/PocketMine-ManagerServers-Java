/* _____           _        _   __  __ _                   __  __                                   _____                              
 *|  __ \         | |      | | |  \/  (_)                 |  \/  |                                 / ____|                             
 *| |__) |__   ___| | _____| |_| \  / |_ _ __   ___ ______| \  / | __ _ _ __   __ _  __ _  ___ _ _| (___   ___ _ ____   _____ _ __ ___ 
 *|  ___/ _ \ / __| |/ / _ \ __| |\/| | | '_ \ / _ \______| |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '__\___ \ / _ \ '__\ \ / / _ \ '__/ __|
 *| |  | (_) | (__|   <  __/ |_| |  | | | | | |  __/      | |  | | (_| | | | | (_| | (_| |  __/ |  ____) |  __/ |   \ V /  __/ |  \__ \
 *|_|   \___/ \___|_|\_\___|\__|_|  |_|_|_| |_|\___|      |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_| |_____/ \___|_|    \_/ \___|_|  |___/
 *                                                                                   __/ |                                             
 *                                                                                  |___/                                              
 *Copyright (C) 2015-2016 @author matcracker
 *
 *This program is free software: you can redistribute it and/or modify 
 *it under the terms of the GNU Lesser General Public License as published by 
 *the Free Software Foundation, either version 3 of the License, or 
 *(at your option) any later version.
*/
	
package com.matcracker.PMManagerServers.settings;

import java.io.File;
import java.io.IOException;

import com.matcracker.PMManagerServers.lang.BaseLang;
import com.matcracker.PMManagerServers.loaders.PluginsLoader;
import com.matcracker.PMManagerServers.utility.Utility;
import com.matcracker.PMManagerServers.utility.UtilityColor;

public class PluginManager {
	
	public static void plugMenu() throws IOException{
		Utility.cleanScreen();
		System.out.println(Utility.softwareName);
		System.out.println(Utility.setTitle("&c", BaseLang.translate("pm.title.pluginManager")));
		System.out.println("1- " + BaseLang.translate("pm.plugManager.list"));
		System.out.println("2- " + BaseLang.translate("pm.plugManager.use"));
		System.out.println("3- " + BaseLang.translate("pm.standard.back"));
		int opt = Utility.readInt(BaseLang.translate("pm.choice.option") + " ", null);
		
		if(opt == 1){
			Utility.showPlugins();
			Utility.waitConfirm(BaseLang.translate("pm.standard.enter"));
		}
		
		if(opt == 2){
			if(PluginsLoader.pluginFound){
				String name = null;
				File plugin;
				do{
					Utility.showPlugins();
					name = Utility.readString(BaseLang.translate("pm.plugManager.name") + ": ", null);
					plugin = new File("plugins" + File.separator + name + ".jar");
					if(!Utility.is_numeric(name) && plugin.exists())
						PluginsLoader.pluginExec(plugin, "execute");
					
				}while(!plugin.exists() || Utility.is_numeric(name));
			}else
				Utility.waitConfirm(UtilityColor.RED + BaseLang.translate("pm.plugins.noPluginFound"));
		}
			
		if(opt == 3)
			Settings.settingsMenu();
		
		plugMenu();
		
		
	}
}
