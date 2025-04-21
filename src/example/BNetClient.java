package example;

import arc.*;
import arc.audio.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.CommandHandler.*;
import arc.util.io.*;
import arc.util.serialization.*;
import arc.util.serialization.JsonValue.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.core.GameState.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.logic.*;
import mindustry.net.Administration.*;
import mindustry.net.*;
import mindustry.net.Packets.*;
import mindustry.world.*;
import mindustry.world.modules.*;

// да я блять просто спиздил импорты с нет клиента, и что?

public class BNetClient extends NetClient {
  
    @Remote(variants = Variant.both, priority = PacketPriority.low, unreliable = true)
    public static void blockSnapshot(short amount, byte[] data){
        try{
            netClient.byteStream.setBytes(data);
            DataInputStream input = netClient.dataStream;

            for(int i = 0; i < amount; i++){
                int pos = input.readInt();
                short block = input.readShort();
                Tile tile = world.tile(pos);
                if(tile == null || tile.build == null){
                    // Log.warn("Missing entity at @. Skipping block snapshot.", tile);
                    break;
                }
                if(tile.build.block.id != block){
                    // Log.warn("Block ID mismatch at @: @ != @. Skipping block snapshot.", tile, tile.build.block.id, block);
                    break;
                }
                tile.build.readSync(Reads.get(input), tile.build.version());
            }
        }catch(Exception e){
            // Log.err(e);
        }
    }
}
