package sudoku;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

public class Main extends Canvas
{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 600, HEIGHT = 500;
	private static Dimension SIZE = new Dimension(WIDTH, HEIGHT);
	
	private static int xCord, yCord;
	public static boolean sqSelected;
	
	
	public static int[][] numbers = {{9,4,6,1,5,3,8,2,7}
									,{5,8,0,7,2,6,4,9,3}
									,{7,3,2,8,4,9,5,1,6}
									,{6,2,8,3,9,5,7,4,1}
									,{1,5,4,6,7,2,9,3,8}
									,{3,7,9,4,1,8,2,6,5}
									,{2,1,5,9,3,7,6,8,4}
									,{8,9,3,5,6,4,1,7,2}
									,{4,6,7,2,8,1,3,5,9}};
	private static boolean error = false;
	
	private difficulty diff = difficulty.MEDIUM;
	
	
	private void generateRandom()
	{
		numbers = new int[9][9];
		Random r = new Random();
		
		int n1 = r.nextInt(9) + 1;
		numbers[0][0] = n1;
		
		for(int i = 0; i  < 50; i++) {
			int x = r.nextInt(9);
			int y = r.nextInt(9);
			int n = r.nextInt(9) + 1;
			
			
			if(isAPossibility(x, y, n)) {
				numbers[x][y] = n;
				if(solve())
					return;
				else
					generateRandom();
			}else
				numbers[x][y] = 0;
		}
		

	}
	
	private void removeRandomPieces() 
	{
		Random r = new Random();
		
		
		int removes = 0;
		switch (diff) {
			case EASY:
				removes = r.nextInt(5) + 35;
				break;
			case MEDIUM:
				removes = r.nextInt(5) + 55;
				break;
			case HARD:
				removes = r.nextInt(5) + 65;
				break;
			case EXPERT:
				removes = r.nextInt(5) + 85;
				break;
			}

		System.out.println(removes);
		
		for(int i = 0; i < removes; i++) {
			int x = r.nextInt(9);
			int y = r.nextInt(9);
			numbers[x][y] = 0;
		}
	}
	
	
	private boolean isAPossibility(int x, int y, int n) 
	{
		for(int i = 0; i < 9; i++) { //row
			 if (numbers[i][y] == n)
				 return false;
		}
		for(int i = 0; i < 9; i++) { //column
			 if (numbers[x][i] == n)
				 return false;
		}
		
		int x0 = (x/3) * 3;
		int y0 = (y/3) * 3;
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				if (numbers[x0 + i][y0 + j] == n)
					return false;
					
		return true;
	}
	
	private boolean solve() 
	{
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(numbers[x][y] == 0) {
					for(int n = 1; n < 10; n++) {
						if (isAPossibility(x, y, n)) {
							numbers[x][y] = n;
							if (solve()) 
								return true;
							else 
								numbers[x][y] = 0;
						}
					}
					return false;
				}
			}
		}	
		return true;
	}
	
	private void selectSq(int x, int y)
	{
		if(!sqSelected) {
			xCord = x;
			yCord = y;
			sqSelected = true;
		} else {
			xCord = -1;
			yCord = -1;
			sqSelected = false;
		}
		
		repaint();
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		Font font = new Font("Tahoma", Font.PLAIN, 20);
		g.setFont(font);
		
		//draw board
		for(int i = 0; i < 10; i++)
			if (i % 3 == 0) {
				g.fillRect(40 + (i * 40) - 1, 40, 2, HEIGHT - 140);
				g.fillRect(39, 40 + (i * 40) - 1, 362, 2);
			} else {
				g.drawLine(40 + (i * 40), 40, 40 + (i * 40), HEIGHT - 100);
				g.drawLine(40, 40 + (i * 40), 400, 40 + (i * 40));
			}
		
		//draw numbers
		for (int i = 0; i < numbers.length; i++)
			for(int j = 0; j < numbers[0].length; j ++)
				if(numbers[i][j] != 0)
					g.drawString("" + numbers[i][j], 40 * i + 55, 40 * j + 67);
		
		//draw solve button
		g.setColor(new Color(0xff228822));
		g.fillRoundRect(450, 200, 100, 40, 5, 5);
		g.setColor(Color.WHITE);
		g.drawString("Solve", 477, 226);
		
		//draw clear button
		g.setColor(new Color(0xff882222));
		g.fillRoundRect(450, 300 - 50, 100, 40, 5, 5); 
		g.setColor(Color.WHITE);
		g.drawString("Clear", 477, 326 - 50);
		
		
		g.setColor(Color.DARK_GRAY);
		int bx = 435;
		int by = 330;
		int xPoints[] = {bx, bx + 15, bx + 15};
		int yPoints[] = {by + 8, by , by + 16};
		g.fillPolygon(xPoints, yPoints, 3);
		int off = 100;
		int xPoints2[] = {30 + bx + off, 15 + bx + off, 15 + bx + off};
		int yPoints2[] = {by + 8, by, by + 16};
		g.fillPolygon(xPoints2, yPoints2, 3);
		
		Rectangle rect = new Rectangle(bx + 15, by-7, 100, 30);
		drawCenteredString(g, diff.name().toString(), rect, font);
		
		if(error) {
			g.setColor(Color.RED);
			g.drawString("Invalid Sudoku!", 10, 20);
		}
		
		//draw selection
		if(sqSelected)
		{
			g.setColor(Color.BLUE);
			g.fillRect(40 + xCord * 40, 40 + yCord * 40, 40, 40);
		}
	}
	
	
	
	public static void main(String[] args)
	{	
		JFrame f = new JFrame("Sudoku Solver");
		f.setMaximumSize(SIZE);
		f.setMinimumSize(SIZE);
		f.setPreferredSize(SIZE);
		
		Main m = new Main();
		f.add(m);
		f.setDefaultCloseOperation(3);
		f.setResizable(false);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
		m.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				int x = e.getX();
				int y = e.getY();
				
				if(x > 40 && x <= 400 && y > 40 && y <= 400) 
					m.selectSq((x - 40) / 40, (y - 40) / 40);
				else 
					if(Main.sqSelected)
						m.selectSq((x - 40) / 40, (y - 40) / 40);
				
				//450, 200, 550, 240
				if(x > 450 && x <= 550 && y > 200 && y <= 240) {
					m.solve();
					for(int i = 0; i < 9; i++)
						for(int j = 0; j < 9; j++)
							if (numbers[i][j] == 0)
								error = true;
				}
					
				
				// 450, 250, 550, 290
				if(x > 450 && x <= 550 && y > 250 && y <= 290) {
					Main.numbers = new int[9][9];
					error = false;
				}
				
				//435,332, 450,348 -> prev
				if(x > 435 && x <= 450 && y > 332 && y <= 348) {
					switch(m.diff) {
					case EASY:
						m.diff = difficulty.EXPERT;
						break;
					case MEDIUM:
						m.diff = difficulty.EASY;
						break;
					case HARD:
						m.diff = difficulty.MEDIUM;
						break;
					case EXPERT:
						m.diff = difficulty.HARD;
						break;
					default:
						break;
					}
				}
				//550,332, 565,348 -> next
				if(x > 550 && x <= 565 && y > 332 && y <= 348) {
					switch(m.diff) {
					case EASY:
						m.diff = difficulty.MEDIUM;
						break;
					case MEDIUM:
						m.diff = difficulty.HARD;
						break;
					case HARD:
						m.diff = difficulty.EXPERT;
						break;
					case EXPERT:
						m.diff = difficulty.EASY;
						break;
					default:
						break;
					}
					
				}
			
				m.repaint();
			}
		});
		
		m.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode();
				if (sqSelected) {
					if(k == KeyEvent.VK_0 || k == KeyEvent.VK_1 || k == KeyEvent.VK_2 
							|| k == KeyEvent.VK_3 || k == KeyEvent.VK_4 || k == KeyEvent.VK_5
							|| k == KeyEvent.VK_6 || k == KeyEvent.VK_7 || k == KeyEvent.VK_8
							|| k == KeyEvent.VK_9) {
						numbers[xCord][yCord] = Integer.parseInt(KeyEvent.getKeyText(k));
						
						m.selectSq(-1, -1);
					}
				}
				
				if(k == KeyEvent.VK_SPACE) {
					m.generateRandom();
					m.removeRandomPieces();
				}
				
				m.repaint();
			}
		});
	}
	
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.setFont(font);
	    g.drawString(text, x, y);
	}
	
	private enum difficulty
	{
		EASY,MEDIUM, HARD, EXPERT;
	}
}
