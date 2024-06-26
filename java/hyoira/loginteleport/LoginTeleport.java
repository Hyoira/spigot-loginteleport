package hyoira.loginteleport;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoginTeleport extends JavaPlugin implements Listener {

    private Location loginLocation;

    @Override
    public void onEnable() {
        // Config 生成
        saveDefaultConfig();
        loadLoginLocation();

        // イベントリスナーを登録
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("setloginpos").setExecutor(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(loginLocation != null){
            player.teleport(loginLocation);
        }
    }

    // コマンド実行時の処理
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("プレイヤーのみがこのコマンドを実行できます。");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("loginteleport.setloginpos")) {
            player.sendMessage("このコマンドを実行する権限がありません。");
            return true;
        }

        // コマンドを実行したプレイヤーの位置をログイン位置に設定
        loginLocation = player.getLocation();
        getConfig().set("loginLocation.world", loginLocation.getWorld().getName());
        getConfig().set("loginLocation.x", loginLocation.getX());
        getConfig().set("loginLocation.y", loginLocation.getY());
        getConfig().set("loginLocation.z", loginLocation.getZ());
        saveConfig();

        player.sendMessage(
                "ログイン場所を "
                + loginLocation.getBlockX() + ", "
                + loginLocation.getBlockY() + ", "
                + loginLocation.getBlockZ() + " に指定しました。"
        );
        return true;
    }

    private void loadLoginLocation() {
        if (getConfig().contains("loginLocation")) {
            String worldName = getConfig().getString("loginLocation.world");
            double x = getConfig().getDouble("loginLocation.x");
            double y = getConfig().getDouble("loginLocation.y");
            double z = getConfig().getDouble("loginLocation.z");
            loginLocation = new Location(getServer().getWorld(worldName), x, y, z);
        }
    }
}
