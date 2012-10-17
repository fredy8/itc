package game.structure;

import game.entities.Block;
import game.entities.Entity;
import game.entities.NPC;
import game.entities.Object;
import game.entities.Portal;
import game.entities.Tile;
import game.entities.item.Item;
import game.entities.superentities.Monster;
import game.entities.superentities.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Slot {

	public static int SIZE; //set by Main class in the game config
	
	private Entity strongEntity;
	private Tile tile;
	private Portal portal;
	private ArrayList<Item> items = new ArrayList<Item>();
	private Object object; //not strong, only its blocks are strong
	
	public List<Entity> getAll() {
		List<Entity> all = new ArrayList<Entity>();
		all.addAll(Arrays.asList(strongEntity, tile, portal, object));
		all.addAll(items);
		all.removeAll(Collections.singleton(null)); //removes all null elements
		return all;
	}

	public void update(){
		for(Entity e: getAll())
			e.update();
	}
	
	public void render(int type, int render){
		
		assert type >= 0 && type <= 4 && render >=0 && render <= 2;
		
		Entity entity = null;
		switch(type){
		case 0:
			entity = tile;
			break;
		case 1:
			entity = portal;
			break;
		case 2:
			for(Item item: items){
				if(render == 0) item.render();
				else if(render == 1) item.midRender();
				else if(render == 2) item.UIRender();
			}
			break;
		case 3:
			entity = strongEntity;
			break;
		case 4:
			entity = object;
			break;
		}
		
		if(entity != null){
			if(render == 0) entity.render();
			else if(render == 1) entity.midRender();
			else if(render == 2) entity.UIRender();
		}
	}
	
	public Entity getStrongEntity(){
		return strongEntity;
	}
	
	public void setStrongEntity(Entity entity){
		assert strongEntity == null; //maker sure no strong entity is being replaced

		if(!entity.isStrong()) 
			return;
		
		strongEntity = entity;
	}
	
	public void removeStrongEntity(){
		strongEntity = null;
	}
	
	public Tile getTile(){
		return tile;
	}
	
	public void setTile(Tile tile){
		this.tile = tile;
	}
	
	public Portal getPortal(){
		return portal;
	}
	
	public void setPortal(Portal portal){
		this.portal = portal;
	}
	
	public Object getObject(){
		return object;
	}
	
	public void setObject(Object object){
		if(!object.isStrong())
			this.object = object;
	}
	
	public List<Item> getItems(){
		return items;
	}
	
	public void addItem(Item item){
		items.add(item);
	}
		
	public void removeItem(int id){
		ListIterator<Item> it = items.listIterator(); //ListIterator must be used instead of iterator when removing items
		while(it.hasNext()){
			if(it.next().id() == id)
				it.remove();
		}
	}
	
	public void remove(Entity entity){
		
		if(entity == tile)
			tile = null;
		else if(entity == portal)
			portal = null;
		else if(entity == strongEntity)
			strongEntity = null;
		else if(entity instanceof Item)
			removeItem(entity.id());
		
	}
	
	public void set(Entity entity){
		if(entity instanceof Tile)
			setTile((Tile)entity);
		else if(entity instanceof Portal)
			setPortal((Portal)entity);
		else if(entity instanceof Object && !entity.isStrong())
			setObject((Object)entity);
		else if(entity.isStrong())
			setStrongEntity(entity);
		else if(entity instanceof Item)
			addItem((Item)entity);
	}
	
	public Monster getMonster(){ 
		if(strongEntity instanceof Monster)
			return (Monster)strongEntity;
		return null;
	}
	
	public NPC getNPC(){ 
		if(strongEntity instanceof NPC)
			return (NPC)strongEntity;
		return null;
	}
	
	public Player getPlayer(){ 
		if(strongEntity instanceof Player)
			return (Player)strongEntity;
		return null;
	}
	
	public Block getBlock(){
		if(strongEntity instanceof Block)
			return (Block)strongEntity;
		return null;
	}
	
	public String toString(){
		return "(" + tile.getX() + ", " + tile.getY() + ") " +  ": [" + tile + ", " + portal + ", " + strongEntity + ", " + items + "]";
	}
	
}
