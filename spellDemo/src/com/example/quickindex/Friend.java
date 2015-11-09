package com.example.quickindex;

public class Friend implements Comparable<Friend>{

	private String name;
	private String spell;

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public Friend(String name) {
		super();
		this.name = name;
		//一开始就转化好拼音
		setSpell(SpellUtil.getSpell(name));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(Friend another) {
		String spell = SpellUtil.getSpell(name);
		String anoSpell = SpellUtil.getSpell(another.getName());
		return spell.compareTo(anoSpell);
	}
}
