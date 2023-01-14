package me.bukkit.teaming;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by arnavguneta.
 */
public class CFile {

	public FileConfiguration cfg;
	private Main plugin;
	private File file;

	public CFile(Main plugin, String fileName) {
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder(), fileName);

		if (!(file.exists())) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.cfg = YamlConfiguration.loadConfiguration(file);
	}

	public void save() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
