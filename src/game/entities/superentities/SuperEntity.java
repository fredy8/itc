package game.entities.superentities;

import game.entities.Entity;
import game.features.Skill;
import game.structure.Slot;
import game.util.Util;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

/**
 * Abstract type for entities that can move, face a direction and attack
 */
public abstract class SuperEntity extends Entity
{

	private int damage, facing = DOWN;
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	private ArrayList<Integer> damages = new ArrayList<Integer>(); // TODO
																	// change to
																	// queue
	private ArrayList<Long> damageTime = new ArrayList<Long>();
	private Texture textures[] = new Texture[4];
	public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

	public SuperEntity(int id)
	{
		super(id);
		setStrong();

		textures[UP] = Util.getTexture(getClass().getSimpleName().toLowerCase() + "/" + hexID() + "/back.png");
		textures[RIGHT] = Util.getTexture(getClass().getSimpleName().toLowerCase() + "/" + hexID() + "/side.png");
		textures[DOWN] = Util.getTexture(getClass().getSimpleName().toLowerCase() + "/" + hexID() + "/front.png");
		textures[LEFT] = textures[RIGHT];

		face(DOWN);
	}

	/**
	 * OVERRIDE getTexture() del Entity
	 */
	
	public Texture getTexture()
	{
		return textures[getFacingDir()];
	}
	
	/**
	 * 
	 * <br>
	 * <b>face</b> <br>
	 * <p>
	 * <tt>protected void face(int dir)</tt>
	 * </p>
	 * Sets the facing direction of <i>this</i> superentity. <br>
	 * <br>
	 * 
	 * @see #UP
	 * @see #RIGHT
	 * @see #DOWN
	 * @see #LEFT
	 */
	protected void face(int dir)
	{
		setFacing(dir);
	}

	public void move(int dir)
	{
		face(dir);

		if (canMove(dir))
		{
			setPosition(Util.addRelPoints(position(), new Point(0, 1), dir));
			// TODO play move animation
		}
	}

	protected boolean canMove(int dir)
	{
		// check the slot were its gonna move so that it has no other strong
		// entity
		int xMove = 0;
		int yMove = 0;

		switch (dir)
		{
		case UP:
			yMove--;
			break;
		case RIGHT:
			xMove++;
			break;
		case DOWN:
			yMove++;
			break;
		case LEFT:
			xMove--;
		}

		Point targetPos = new Point(getX() + xMove, getY() + yMove);

		if (getMap().isPointInMap(targetPos) && getMap().get(targetPos).getStrongEntity() == null)
			return true;

		return false;
	}

	public void update()
	{
		for (Skill s : skills)
		{
			s.update();
		}
	}

	public void render()
	{
		Util.renderEntity(getTexture(), Util.pointArithmetic(-1, position(), getMap().getOffSet()), getRenderOffset(),
				getRenderSize(), 0, getFacingDir() != LEFT ? 0 : 1, 0); //flip texture when facing left
		for (Skill s : skills)
			s.render();
	}

	public void UIRender()
	{ // superentities can be attacked, so their damage is displayed above them
		// at an UI level
		for (int i = 0; i < damages.size(); i++)
		{
			if (damageTime.get(i) > System.currentTimeMillis())
			{
				float xpos = (Util.getTextWidth(damages.get(i).toString()) - Slot.SIZE) / 2;
				Util.useFont("Arial", Font.BOLD, 10, Color.white);
				Util.write(
						damages.get(i).toString(),
						((getX() - getMap().getOffSet().getX()) * Slot.SIZE - xpos),
						(getY() - getMap().getOffSet().getY()) * Slot.SIZE - 20 - Util.getFontHeight()
								* (damages.size() - 1) + i * Util.getFontHeight());
			} else
			{
				damages.remove(i);
				damageTime.remove(i);
				i--;
			}
		}
	}

	protected void attack(int skill)
	{
		getSkill(skill).attack();
	}

	/**
	 * 
	 * <br>
	 * <b>hit</b> <br>
	 * <p>
	 * <tt>public boolean hit(int damage)</tt>
	 * </p>
	 * Does <i>damage</i> to <i>this</i> superentity. Returns true if the
	 * superentity died. <br>
	 * <br>
	 */
	public boolean hit(int damage)
	{ // get hit
		setHP(getHP() - damage);
		if (getHP() <= 0)
		{
			setHP(0);
			die();
			return true;
		}
		damages.add(damage); // to render the damage
		damageTime.add(System.currentTimeMillis() + 1700); // TODO? use thread?
		return false;
	}

	protected void resetDamages()
	{
		damages.clear();
		damageTime.clear();
	}

	public void die()
	{
		for (Skill s : skills)
			s.stopAll();

		if (!(this instanceof Player))
		{ // TODO handle player dead
			getMap().get(position()).removeStrongEntity();
		}
	}

	public void addSkill(int ID)
	{
		skills.add(new Skill(ID, this));
	}

	public Skill getSkill(int ID)
	{
		for (Skill s : skills)
		{
			if (s.id() == ID)
				return s;
		}
		return null;
	}

	public int getDamage()
	{ // returns a damage based on its average damage with a normal distribution
		// with a 15% deviation
		double avgDmg = getAverageDamage();
		double deviation = avgDmg * .15;
		double damage = new Random(System.nanoTime()).nextGaussian() * deviation + avgDmg;
		return (int) (damage + .5); // +.5 to round and not truncate
	}

	protected double getAverageDamage()
	{
		return damage; // overrode by the player to depend on its stats
	}

	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	public int getFacingDir()
	{
		return facing;
	}

	public void setFacing(int dir)
	{
		facing = dir;
	}

	public void stopAllActions()
	{
		for (Skill skill : skills)
		{
			skill.stopAll();
		}
	}

	public abstract int getHP();

	public abstract void setHP(int hp);
}
