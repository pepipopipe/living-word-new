package me.poernomo.livingword;

public class Verse {
	private String passage, content;

	public Verse() {
		this.passage = null;
		this.content = null;
	}

	public Verse(String verse, String content) {
		this.passage = verse;
		this.content = content;
	}

	public String getContent() {

		return content;
	}

	public String getPassage() {
		// TODO Auto-generated method stub
		return passage;
	}

}
