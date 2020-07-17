package test;

public class Player {
	
	// 基本信息
	public int id;
	public String name;
	
	// 游戏相关
	public int[] visibleCard;
	public int[] unvisibleCard;
	public int p;// 发到了第几张牌
	public int coin;
	public int needMinPrice;
	public int score;
	
	public Player(int id) {
		this.id = id;
		this.visibleCard = new int[4];
		this.unvisibleCard = new int[2];
		p = 0;
		score = 0;
		coin = 18;
		this.needMinPrice = 1;
	}
	// 玩家获得暗牌
	public void receiveUnvisibleCard(int cardId1, int cardId2) {
		unvisibleCard[0] = cardId1;
		unvisibleCard[1] = cardId2;
	}
	public void receiveVisibleCard(int cardId) {
		if(p == 4) return;
		this.visibleCard[p] = cardId;
		p++;
		Judge.sort(this.visibleCard, p);
	}
	public int pay(int x) {
		int res = x + needMinPrice;
		this.coin -= res;
		this.needMinPrice = res;
		return res;
	}
	public String cardToString(Deck deck) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<p;i++) {
			sb.append(deck.getCard(this.visibleCard[i]));
			sb.append(",");
		}
		return sb.toString();	
	}

}
