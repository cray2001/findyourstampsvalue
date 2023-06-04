package com.findyourstampsvalue.aqa.util;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class HideMe {

	@SerializedName("hideme")
	private List<HideMeItem> hideMeItemList;
}