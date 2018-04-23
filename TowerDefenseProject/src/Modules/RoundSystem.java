package Modules;

import Kind.EnemyType;

public class RoundSystem {
	
	public enum Round {
		
		GameWillBeStart(0, EnemyType.Level1, 0, 2000, 100),
		Round1(10, EnemyType.Level1, 1000, 5000, 100),
		Round2(10, EnemyType.Level1, 800, 5000, 100),
		Round3(10, EnemyType.Level2, 1000, 5000, 100),
		Round4(10, EnemyType.Level2, 500, 5000, 100),
		Infinity(50, EnemyType.Level2, 300, 5000, 100);
			
		private int enemySize;
		private EnemyType enemyType;
		private int enemySummonDelay;
		
		private int brakeTime;
		private int bonusMoney;
		
		private Round(int enemySize, EnemyType enemyType, int enemySummonDelay, int brakeTime, int bonusMoney) {
			this.enemySize = enemySize;
			this.enemyType = enemyType;
			this.enemySummonDelay = enemySummonDelay;
			this.brakeTime = brakeTime;
			this.bonusMoney = bonusMoney;
		}

		public int getEnemySize() {
			return enemySize;
		}

		public EnemyType getEnemyType() {
			return enemyType;
		}

		public int getEnemySummonDelay() {
			return enemySummonDelay;
		}

		public int getBrakeTime() {
			return brakeTime;
		}

		public int getBonusMoney() {
			return bonusMoney;
		}		
	}
	
	private TowerDefenseMangager manager;
	private Round round;
	private int delay = 0;
	private int summonedEnemy = 0;
	
	public RoundSystem(TowerDefenseMangager manager)
	{
		this.manager = manager;
		this.round = Round.GameWillBeStart;
		this.summonedEnemy = 0;
		this.delay = round.getEnemySummonDelay();
	}
	
	public void update(int period)
	{
		//Still summon enemy
		if(summonedEnemy < round.getEnemySize())
		{
			if(delay >= round.getEnemySummonDelay())
			{
				manager.putEnemy(round.getEnemyType());
				summonedEnemy++;
				delay = 0;
			}
		}
		else
		{
			if(delay >= round.getBrakeTime())
			{
				//move next round
				if(round == Round.GameWillBeStart) round = Round.Round1;
				else if(round == Round.Round1) round = Round.Round2;
				else if(round == Round.Round2) round = Round.Round3;
				else if(round == Round.Round3) round = Round.Round4;
				else if(round == Round.Round4) round = Round.Infinity;
				
				summonedEnemy = 0;
				delay = round.getEnemySummonDelay();
			}
		}
		
		delay += period;
		
	}

	public Round getRound() {
		return round;
	}
	
	
	
	
	

}
