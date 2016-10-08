package io.github.hsyyid.polis.utils;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.hsyyid.polis.Polis;

public enum CanClaimResult
{
	YES(Text.builder().append(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location for " + ConfigManager.getClaimCost() + " ")).append(Polis.economyService.getDefaultCurrency().getPluralDisplayName()).build()),
	ALREADY_CLAIMED(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is already claimed!")),
	OTHER_CLAIMED(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is claimed by another town!")),
	INSUFFICIENT_FUNDS(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "Not enough funds! Deposit funds or setup taxes!")),
	NO_PERMISSION(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader or an executive to claim!")),
	TOWN_SIZE(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "You've reached the claim cap. Add more people to your town!")),
	MAX_CLAIMS(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "You already have the maximum number of claims!")),
	NO_TOWN(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!")),
	ERROR(Text.of(Utils.polisPrefix(), TextColors.DARK_RED, "Error! ", TextColors.RED, "An unknown error occurred!"));

	public final Text text;
	
	private CanClaimResult(Text text)
	{
		this.text = text;
	}
}
