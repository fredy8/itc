package game.ui.window;

import game.entities.item.EquipItem;
import game.entities.item.EquipItem.EquipType;
import game.structure.MapManager;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Equipment extends Window{
	
	private static final Map<EquipType, Point> positions = new HashMap<EquipType, Point>();
	
	static
	{
		positions.put(EquipType.HELMET, new Point(74, 45));
		positions.put(EquipType.TOPWEAR, new Point(74, 96));
		positions.put(EquipType.BOTTOMWEAR, new Point(74, 145));
		positions.put(EquipType.SHOES, new Point(74, 194));
		positions.put(EquipType.WEAPON, new Point(16, 96));
		positions.put(EquipType.SHIELD, new Point(126, 96));

	}
	
	public Equipment ()
	{
		super(new Point(100, 100), new Dimension(174, 256));
	}
	
	public void render()
	{
		super.render();
		
		for(Map.Entry<EquipType, EquipItem> equip : MapManager.getMap().getPlayer().getEquips().entrySet())
		{
			Point position = positions.get(equip.getKey());
			equip.getValue().render(position.getX() + getX(), position.getY() + getY());
		}		
	}
	
	
	@Override
	protected void mouse() {
		// TODO Auto-generated method stub
		
	}
	
	int getKey()
	{
		return Keyboard.KEY_E;
	}

}
