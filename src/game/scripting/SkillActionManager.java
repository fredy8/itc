package game.scripting;

import game.entities.superentities.Monster;
import game.entities.superentities.Player;
import game.features.SkillAttack;
import game.structure.Map;
import game.structure.MapManager;
import game.structure.Slot;
import game.util.Util;

import org.lwjgl.util.Point;

/**
 * The object passed to the skill scripts. This object's public methods can be
 * used in the script.
 */
public class SkillActionManager extends AbstractScriptManager
{

	private SkillAttack activeAttack;
	private Point origin;
	private int facingDir;

	public SkillActionManager(SkillAttack sa)
	{
		activeAttack = sa;
		facingDir = sa.getSkill().getAttacker().getFacingDir();
		origin = sa.getSkill().getAttacker().position();
	}

	public void hit(Point location[], float dmg)
	{
		for (Point p : location)
		{
			Point pos = Util.addRelPoints(origin, p, facingDir);
			if (activeAttack.getSkill().getAttacker() instanceof Player)
			{
				Map map = MapManager.getMap();
				Monster monster = map.get(pos).getMonster();
				if (monster != null)
				{
					monster.hit((int) (getPlayer().getDamage() * dmg + .5f));
				}
			} else if (activeAttack.getSkill().getAttacker() instanceof Monster)
			{
				if (getPlayer().position().equals(pos))
					getPlayer().hit((int) (activeAttack.getSkill().getAttacker().getDamage() * dmg + .5f));
			}

		}
	}

	public void stop()
	{
		activeAttack.stop();
	}

	public void setOrigin(Point position)
	{
		origin = position;
	}

	public boolean hasMonsterAt(Point p)
	{
		if (activeAttack.getSkill().getAttacker() instanceof Player)
		{
			Point pos = Util.addRelPoints(origin, p, facingDir);
			for (Slot s : MapManager.getMap().getAllSlots())
			{
				Monster monster = s.getMonster();
				if (monster != null)
					if (monster.position().equals(pos))
						return true;
			}
		}
		if (activeAttack.getSkill().getAttacker() instanceof Monster)
			return getPlayer().position().equals(Util.addRelPoints(origin, p, facingDir));
		return false;
	}
	
	public boolean hasStrongEntityAt(Point position)
	{
		Slot slot = MapManager.getMap().get(Util.addRelPoints(origin, position, facingDir));
		return slot == null ? true : slot.getStrongEntity() != null;
	}

	public String[] getVariables(int i)
	{
		return activeAttack.getSkill().getVariables(i);
	}

	public int getLevel()
	{
		return activeAttack.getSkill().getLevel();
	}

	public void play(Point p)
	{
		activeAttack.play(Util.addRelPoints(origin, p, facingDir));
	}
}
