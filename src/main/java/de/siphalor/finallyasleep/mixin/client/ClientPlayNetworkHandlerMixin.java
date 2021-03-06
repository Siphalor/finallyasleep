package de.siphalor.finallyasleep.mixin.client;

import de.siphalor.finallyasleep.FinallyAsleep;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(method = "onGameJoin", at = @At("RETURN"))
	public void onGameJoin(GameJoinS2CPacket gameJoinPacket, CallbackInfo callbackInfo) {
		ByteBuf buf = Unpooled.buffer();
		ClientSidePacketRegistry.INSTANCE.sendToServer(FinallyAsleep.MOD_PRESENT_C2S_PACKET, new PacketByteBuf(buf));
	}
}
