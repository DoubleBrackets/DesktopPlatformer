package gameObjects;

public abstract class gameObjectEnemy extends gameObjectPhysics{

	public gameObjectEnemy(String name, int width, int height) {
		super(name, width, height, 5);
	}
	@Override
	public void UpdateGameObject()
	{
		EnemyUpdate();
		if(physicsApplied)
			updatePhysics();
	}
	public abstract void EnemyUpdate();
	@Override
	public void onCollisionTo(gameObjectPhysics g) 
	{
		if(g.killable == true && g.objectName == "Player")
			g.Destroy();
	}
	public void onCollisionFrom(gameObjectPhysics g) {
		if(g.killable == true && g.objectName == "Player")
			g.Destroy();
	}

}
