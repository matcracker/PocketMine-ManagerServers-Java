package com.matcracker.PMManagerServers.Loaders;

import java.io.File;
import java.io.IOException;

import com.matcracker.PMManagerServers.API.InstallatorAPI;
import com.matcracker.PMManagerServers.API.UtilityServersAPI;
import com.matcracker.PMManagerServers.Languages.BaseLang;
import com.matcracker.PMManagerServers.Languages.LangSelector;
import com.matcracker.PMManagerServers.Utility.Utility;

public class Loader {
  /** _____           _        _   __  __ _                   __  __                                   _____                              
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
		
	public static void startLoader(){
		String[] dirsName = {
				"Data",
				"ServersName",
				"Path",
				"Performance",
				"Utils", 
				"Installations",
				"Installations" + File.separator + "Status",
				"Installations" + File.separator + "Version",
				"Languages",
				"Backups",
				"Backups" + File.separator + "Status",
				"Backups" + File.separator + "Servers"
		};
		
		File checkLicense = new File("LICENSE.pdf");
		File dirMaker;
				
		boolean[] firstStart = new boolean[dirsName.length];
		
		for(int i = 0; i < dirsName.length; i++){
			firstStart[i] = false;
			dirMaker = new File(dirsName[i]);
			if(!dirMaker.exists()){
				firstStart[i] = true;
				dirMaker.mkdir(); //Make first directory for the start.pm
			}
		}
		
		if(!firstStart[(int)(Math.random() * dirsName.length)] && checkLicense.exists() && UtilityServersAPI.checkServersFile("Data", "langSel", -1)){
			return;
		}else{
			try{
				System.out.println("&fPreparing the first start...");
				
				Thread.sleep(500);
	
				for(int i = 1; i < dirsName.length; i++){
					dirMaker = new File(dirsName[i]);
					dirMaker.mkdir();
				}
				
				System.out.print("&3[");
				for(int i = 0; i <= 77; i++){
					System.out.print("=");
					Thread.sleep(150);
				}
				System.out.print("]");
				
				Thread.sleep(1000);
				completeLoader();
				
			}catch (InterruptedException | IOException e) {
				System.err.println(Utility.generalError);
			}
		}
	}
				
	public static void completeLoader() throws IOException{
		int nservers = 0;
		if(UtilityServersAPI.checkServersFile("Data", "nservers", -1) && UtilityServersAPI.checkServersFile("Data", "langSel", -1)){
			nservers = Utility.readIntData(new File("Data" + File.separator + "nservers.pm"));
			return;
		}else{	
			do{
				LangSelector.langMenu();
				Utility.cleanScreen();
				System.out.println(Utility.softwareName);
				System.out.println(BaseLang.translate("pm.title.completeLoad"));
				System.out.print(BaseLang.translate("pm.chooise.servers") + " <1/2/3/...> : ");
				
				try{
					nservers = Integer.valueOf(Utility.keyword.readLine());
				
				}catch (IOException e){
					System.out.println(Utility.inputError);
				}
				
				if(nservers <= 0){
					System.out.println(BaseLang.translate("pm.errors.fewServers"));
					Utility.keyword.readLine();
				}
			}while(nservers <= 0);
			
			UtilityServersAPI.setNumberServer(nservers);
			
		}
			
		Utility.cleanScreen();
		System.out.println(Utility.softwareName);
		System.out.println(BaseLang.translate("pm.title.completeLoad"));
		System.out.printf(BaseLang.translate("pm.loader.caution") + " '%s'\n", Utility.defaultServersName);
		
		if(nservers >= 1){
			String[] nameServers = new String[nservers];
			String[] path = new String[nservers];
			if(UtilityServersAPI.checkServersFile("ServersName", "ServerName_", nservers - 1)){
				return;
			}else{
				Utility.selection(nservers, nameServers, path);

				for(int i = 1; i <= nservers; i++){
					UtilityServersAPI.setNameServer(i - 1, nameServers[i-1]);
					InstallatorAPI.setStatus(BaseLang.translate("pm.status.noDownload"), i-1);
					InstallatorAPI.setVersion(BaseLang.translate("pm.status.noVersion"), i-1);
					
					UtilityServersAPI.setPath(i - 1, path[i-1]);
				}
				
			}
		}else{
			System.out.println(Utility.generalError);
		}
		
		System.out.println(BaseLang.translate("pm.loader.complete"));
		Utility.keyword.readLine();

	}
}
