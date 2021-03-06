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
	
package com.matcracker.PMManagerServers.commands;

import java.io.File;

import com.matcracker.PMManagerServers.API.UtilityServersAPI;
import com.matcracker.PMManagerServers.lang.BaseLang;
import com.matcracker.PMManagerServers.utility.Utility;
import com.matcracker.PMManagerServers.utility.UtilityColor;

public class CommandStart {
	public static void command(String[] args){
		try{
			if(args.length > 1){
				if(args[1].equalsIgnoreCase("all")){
					for(int i = 1; i <= UtilityServersAPI.getNumberServers(); i++){
						if(UtilityServersAPI.checkServersFile("Path", "path_", i)){
							String pathContent = UtilityServersAPI.getPath(i);
							if(pathContent != null)
								Utility.openSoftware("software", pathContent + File.separator + Utility.getStartName());
							else
								Utility.waitConfirm(UtilityColor.RED + BaseLang.translate("pm.errors.pathNull"));
						}else
							Utility.waitConfirm(UtilityColor.RED + BaseLang.translate("pm.errors.pathNotFound"));
					}
				}else{
					int server = -1;
					if(Utility.is_numeric(args[1])){
						server = Integer.parseInt(args[1]);
					}else{
						for(int i = 1; i <= UtilityServersAPI.getNumberServers(); i++){
							if(args[1].equalsIgnoreCase(UtilityServersAPI.getNameServer(i))){
								server = i;
								break;
							}
						}
					}
					
					if(UtilityServersAPI.checkServersFile("Path", "path_", server)){
						String pathContent = UtilityServersAPI.getPath(server);
						if(pathContent != null)
							Utility.openSoftware("software", pathContent + File.separator + Utility.getStartName());
						else
							Utility.waitConfirm(UtilityColor.RED + BaseLang.translate("pm.errors.pathNull"));
					}else
						Utility.waitConfirm(UtilityColor.RED + BaseLang.translate("pm.errors.pathNotFound"));
				}
			}else
				System.out.println(BaseLang.translate("pm.cmdMode.tooFew"));
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(BaseLang.translate("pm.cmdMode.tooFewMuch"));
		}
	}
}
