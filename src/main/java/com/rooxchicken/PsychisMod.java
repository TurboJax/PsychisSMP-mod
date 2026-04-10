package com.rooxchicken;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PsychisMod implements ModInitializer {
   public static final Logger LOGGER = LoggerFactory.getLogger("psychis-mod");

   public void onInitialize() {
      LOGGER.info("1987 custom keybinds supported! (made by roo)");
   }
}
