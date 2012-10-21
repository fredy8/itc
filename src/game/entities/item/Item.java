package game.entities.item;

import game.entities.Entity;
import game.entities.EntityType;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.util.Point;

/**
 * Abstract type for items.
 */
public abstract class Item extends Entity
{

	private String NAME, DESCRIPTION;
	private int quantity;

	public Item(int id, int amount)
	{
		super(id);
		quantity = amount;

		parseItem(new XMLParser(EntityType.getType(id()) + "/" + hexID() + "/data.xml"));

		setTexture(getMap().getTextureManager().get(id()));
	}

	protected void parseItem(XMLParser parser)
	{
		NAME = parser.getAttribute(getClass().getSimpleName(), "name");
		DESCRIPTION = parser.getAttribute(getClass().getSimpleName(), "description");
	}

	public Item(int id, Point pos)
	{
		this(id, 1);
	}

	public Item(int id)
	{
		this(id, 1);
	}

	public String getName()
	{
		return NAME;
	}

	public String getDescription()
	{
		return DESCRIPTION;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int amount)
	{
		quantity = amount;
	}

	public void add(int num)
	{
		quantity += num;
	}

	public void setPosition(Point pos)
	{
		if (position() != null)
		{

			List<Item> items = getMap().get(position()).getItems();

			for (ListIterator<Item> l = items.listIterator(); l.hasNext();)
			{
				if (l.next().equals(this))
					l.remove();
			}
		}

		getMap().get(pos).addItem(this);
		modifyPos(new Point(pos));
	}

	public String toString()
	{
		return getName();
	}
	
	public static List<Item> stack(List<Item> items)
	{
		List<Item> stackedItems = new ArrayList<Item>();
		for(Item i: items){
			if(stackedItems.contains(i) && EntityType.getType(i.id()) != EntityType.EquipItem)
				stackedItems.get(stackedItems.indexOf(i)).add(i.getQuantity());
			else
				stackedItems.add(i);
		}
		
		return stackedItems;
		
	}

}
