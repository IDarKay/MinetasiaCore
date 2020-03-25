package fr.idarkay.minetasia.core.spigot.executor;

import fr.idarkay.minetasia.core.api.PlayerStatue;
import fr.idarkay.minetasia.core.spigot.MinetasiaCore;
import fr.idarkay.minetasia.core.spigot.user.MinePlayer;
import fr.idarkay.minetasia.core.spigot.utils.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * File <b>SocialSpyExecutor</b> located on fr.idarkay.minetasia.core.spigot.Executor
 * SocialSpyExecutor is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2019 MinetasiaCore.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 12/12/2019 at 22:42
 */
public class SocialSpyExecutor implements CommandExecutor {

    private final static String UTILS_CHAT_SOCIALSPY = "utils.chat.socialspy";
    private final MinetasiaCore plugin;

    public SocialSpyExecutor(MinetasiaCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull  String[] args) {
        if(sender.hasPermission(UTILS_CHAT_SOCIALSPY) && sender instanceof Player)
        {
            final String lang = plugin.getPlayerLang(((Player) sender).getUniqueId());
            UUID u = ((Player) sender).getUniqueId();

            final MinePlayer player = Objects.requireNonNull(plugin.getPlayerManager().get(u));

            final long i = player.getStatus();

            try {
                if(!plugin.isBollTrue(i, PlayerStatue.SOCIAL_SPY.by))
                {
                    player.putData("status", plugin.setBoolIsValue(i, PlayerStatue.SOCIAL_SPY.by, true));
                    sender.sendMessage(Lang.SOCIAL_SPU_ON.get(lang));
                    plugin.socialSpyPlayer.add((Player) sender);
                } else
                {
                    player.putData("status",plugin.setBoolIsValue(i, PlayerStatue.SOCIAL_SPY.by, false));
                    sender.sendMessage(Lang.SOCIAL_SPU_OFF.get(lang));
                    plugin.socialSpyPlayer.remove((Player) sender);
                }
                return true;
            } catch (IllegalArgumentException ignore) {}

        }
        return true;
    }
}
