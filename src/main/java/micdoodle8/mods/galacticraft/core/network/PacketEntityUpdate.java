package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.api.vector.Vector2;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PacketEntityUpdate implements IPacket
{
	private int entityID;
	private Vector3 position;
	private float rotationYaw;
	private float rotationPitch;
	private Vector3 motion;
	private boolean onGround;

	public PacketEntityUpdate()
	{
	}

	public PacketEntityUpdate(int entityID, Vector3 position, Vector2 rotation, Vector3 motion, boolean onGround)
	{
		this.entityID = entityID;
		this.position = position;
		this.rotationYaw = (float) rotation.x;
		this.rotationPitch = (float) rotation.y;
		this.motion = motion;
		this.onGround = onGround;
	}

	public PacketEntityUpdate(Entity entity)
	{
		this(entity.getEntityId(), new Vector3(entity.posX, entity.posY, entity.posZ), new Vector2(entity.rotationYaw, entity.rotationPitch), new Vector3(entity.motionX, entity.motionY, entity.motionZ), entity.onGround);
	}

	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(this.entityID);
		buffer.writeDouble(this.position.x);
		buffer.writeDouble(this.position.y);
		buffer.writeDouble(this.position.z);
		buffer.writeFloat(this.rotationYaw);
		buffer.writeFloat(this.rotationPitch);
		buffer.writeDouble(this.motion.x);
		buffer.writeDouble(this.motion.y);
		buffer.writeDouble(this.motion.z);
		buffer.writeBoolean(this.onGround);
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.entityID = buffer.readInt();
		this.position = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		this.rotationYaw = buffer.readFloat();
		this.rotationPitch = buffer.readFloat();
		this.motion = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		this.onGround = buffer.readBoolean();
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		this.setEntityData(player.worldObj);
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		this.setEntityData(player.worldObj);
	}

	private void setEntityData(World world)
	{
		Entity entity = world.getEntityByID(this.entityID);

		if (entity instanceof EntityBuggy)
		{
			EntityBuggy controllable = (EntityBuggy) entity;
			controllable.setPositionRotationAndMotion(this.position.x, this.position.y, this.position.z, this.rotationYaw, this.rotationPitch, this.motion.x, this.motion.y, this.motion.z, this.onGround);
		}
	}
}
