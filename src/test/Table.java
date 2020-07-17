package test;

import java.util.Scanner;

public class Table {
	// 有i个玩家发x[i]张牌
	public static final int[] player2card = {0, 3, 6, 10, 10};
	public static final int[] need2del = {0, 1, 1, 3, 2};
	public static final int[][] basicprice = {
			{0},
			{0,0,1},
			{0,0,0,1,1,2},
			{0,0,0,0,1,1,1,2,2,3},
			{0,0,0,0,1,1,2,2,2,3}
	};// basicprice[i][j] 代表有i个玩家时，场上第j张牌的基本价格
	
	public int[] tableCards;// 桌面上的扑克牌id// 0-3为1元 4-6为2元 7-8为3元 9为4
	public int cardCount;// 桌面上已经有的扑克牌张数
	public int maxCardCount;// 桌面上能有的最大张数
	
	public Deck deck;// 卡组
	public int maxPlayerCount;
	public int playerCount;// 当前局玩家总人数
	public int[] players; // 玩家id
	public int[] order;
	// 开始进行一轮游戏的初始化
	public Table(int maxPlayerCount) {
		this.cardCount = 0;
		this.playerCount = 0;
		this.maxPlayerCount = maxPlayerCount;
		this.maxCardCount = player2card[playerCount];
		this.tableCards = new int[maxCardCount];
		this.players = new int[playerCount];
		this.order = new int[playerCount];
		for(int i=0;i<order.length;i++) {
			order[i] = i;
		}
		this.deck = new Deck();
		deck.shuffle(10);
	}
	public void addCard(int card) {
		this.tableCards[cardCount] = card;
		cardCount++;
	}
	public void delCards() {
		int toDel = need2del[playerCount];
		int[] tmp = new int[maxCardCount];
		int p = 0;
		for(int i=0;i<maxCardCount;i++) {
			if(tableCards[i]!=-1 && toDel<=0) {
				tmp[p] = this.tableCards[i];
				p++;
			}else {
				if(tableCards[i]!=-1)toDel--;
			}
		}
		this.tableCards = tmp;
		this.cardCount = maxCardCount - playerCount - need2del[playerCount];
	}
	public void delCard(int i) {
		this.tableCards[i] = -1;
		this.cardCount -= 1;
	}
	
	public void addPlayer(int playerid) {
		if(playerCount == 4) return;
		int[] tmp = new int[playerCount + 1];
		for(int i=0;i<playerCount;i++) {
			tmp[i] = this.players[i];
		}
		tmp[playerCount] = playerid;
		this.players = tmp;
		this.playerCount++;
		this.maxCardCount = player2card[playerCount];
		this.tableCards = new int[maxCardCount];
	}
	public void delPlayer(int playerid) {
		if(playerCount == 0)return;
		int[] tmp = new int[playerCount - 1];
		int p = 0;
		for(int i=0;i<playerCount;i++) {
			if(players[i] != playerid) {
				tmp[p] = this.players[i];
				p++;
			}
		}
		this.players = tmp;
		this.playerCount --;
		this.maxCardCount = player2card[playerCount];
		this.tableCards = new int[maxCardCount];
	}
	// 输入参数为第几张牌
	public int cardPrice(int p) {
		switch(this.cardCount) {
		case 10:
			if(p >= 0 && p<=3) {
				return 0;
			}else if(p >=4 && p<=6) {
				return 1;
			}else if(p >=7 && p<=8) {
				return 2;
			}else {
				return 3;
			}
		case 6:
			if(p >= 0 && p<=2) {
				return 0;
			}else if(p >=3 && p<=4) {
				return 1;
			}else {
				return 2;
			}
		case 3:
			if(p >= 0 && p<=1) {
				return 0;
			}else{
				return 1;
			}
		}
		return 0;
	}
	public void startGame(Deck d, int PlayerCount) {
		
	}
	public String cardToString() {
		StringBuilder sb = new StringBuilder();
		if(this.playerCount == 4) {
			for(int i=0;i<4;i++) {
				sb.append(deck.getCard(this.tableCards[i]));
				sb.append(",");
			}
			sb.append('\n');
			for(int i=4;i<7;i++) {
				sb.append(deck.getCard(this.tableCards[i]));
				sb.append(",");
			}
			sb.append('\n');
			for(int i=7;i<9;i++) {
				sb.append(deck.getCard(this.tableCards[i]));
				sb.append(",");
			}
			sb.append('\n');
			sb.append(deck.getCard(this.tableCards[9]));
			return sb.toString();
		}else {
			return "";
		}
	}
	public void playerResort(Judge judge, Player[] players, int n) {
		for(int i=0;i<playerCount;i++) {
			for(int j=0;j<playerCount-1;j++) {
				boolean x = judge.comparePlayerCard(deck.cards, 
						players[this.players[j]], players[this.players[j+1]], n);
				if(x) {
					int tmp = this.players[j];
					this.players[j] = this.players[j+1];
					this.players[j+1] = tmp;
				}
			}
		}
	}
	public static void main(String args[]) {
		Table table = new Table(4);
		Player players[] = new Player[4];
		Judge judge = new Judge();
		for(int i=0;i<4;i++) {
			players[i] = new Player(i);
			table.addPlayer(i);
		}
		System.out.println("游戏开始");
		Scanner sc = new Scanner(System.in);
		// 分发手牌
		for(int i=0;i<4;i++) {
			players[i].receiveUnvisibleCard(table.deck.nextCard(), table.deck.nextCard());
		}
		// 每回合发场面上的牌,一共四个回合
		for(int i=0;i<4;i++) {
			System.out.println("=====================================");
			// 四个人的话场面上发10张牌
			for(int j=table.cardCount;j<table.maxCardCount;j++) {
				table.addCard(table.deck.nextCard());
			}
			System.out.println(table.cardToString());
			System.out.println("--------------------------------");
			for(int j=0;j<4;j++) {
				// 玩家拿牌
				int x = 0;
				if(table.players[j] == 0) {
					System.out.println("你的手牌为" + table.deck.cards[players[0].unvisibleCard[0]]);
					x = sc.nextInt();
				}else {// bot拿牌
					x = 0;
					while(table.tableCards[x] == -1) {
						x++;
					}
				}
				players[table.players[j]].receiveVisibleCard(table.tableCards[x]);
				players[table.players[j]].pay(Table.basicprice[4][x]);
				System.out.println(table.players[j] + "那的牌为" + table.deck.getCard(table.tableCards[x]));
				table.delCard(x);
				System.out.print(table.players[j] + "明面牌为" + players[table.players[j]].cardToString(table.deck));
				System.out.print(table.players[j] + "金币为" + players[table.players[j]].coin + "\n");
				System.out.println(judge.simpleJudgeLevelId(table.deck.cards, players[table.players[j]].visibleCard, i+1));
				System.out.println("--------------------------------");
				System.out.println(table.cardToString());
			}
			// 删除掉多余的牌
			table.delCards();
			// 对玩家进行排序
			table.playerResort(judge, players, i+1);
		}
		
	}
}
