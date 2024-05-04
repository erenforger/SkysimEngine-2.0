/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package vn.giakhanhvn.skysim.features.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.features.item.MaterialQuantifiable;
import vn.giakhanhvn.skysim.features.item.Recipe;
import vn.giakhanhvn.skysim.features.item.SItem;
import vn.giakhanhvn.skysim.features.item.SMaterial;
import vn.giakhanhvn.skysim.util.SUtil;

public class ShapedRecipe
extends Recipe<ShapedRecipe> {
    public static final List<ShapedRecipe> CACHED_RECIPES = new ArrayList<ShapedRecipe>();
    protected String[] shape;
    private final Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<Character, MaterialQuantifiable>();

    public ShapedRecipe(SItem result, boolean usesExchangeables) {
        super(result, usesExchangeables);
        CACHED_RECIPES.add(this);
    }

    public ShapedRecipe(SItem result) {
        this(result, false);
    }

    public ShapedRecipe(SMaterial material, int amount) {
        this(SUtil.setSItemAmount(SItem.of(material), amount));
    }

    public ShapedRecipe(SMaterial material) {
        this(SItem.of(material));
    }

    public ShapedRecipe shape(String ... lines) {
        this.shape = lines;
        return this;
    }

    @Override
    public ShapedRecipe setResult(SItem result) {
        this.result = result;
        return this;
    }

    @Override
    public List<MaterialQuantifiable> getIngredients() {
        return new ArrayList<MaterialQuantifiable>(this.ingredientMap.values());
    }

    public ShapedRecipe set(char k, MaterialQuantifiable material) {
        this.ingredientMap.put(Character.valueOf(k), material.clone());
        return this;
    }

    public ShapedRecipe set(char k, SMaterial material, int amount) {
        return this.set(k, new MaterialQuantifiable(material, amount));
    }

    public ShapedRecipe set(char k, SMaterial material) {
        return this.set(k, new MaterialQuantifiable(material));
    }

    public MaterialQuantifiable[][] toMQ2DArray() {
        MaterialQuantifiable[][] materials = new MaterialQuantifiable[3][3];
        String l1 = SUtil.pad(SUtil.getOrDefault(this.shape, 0, "   "), 3);
        String l2 = SUtil.pad(SUtil.getOrDefault(this.shape, 1, "   "), 3);
        String l3 = SUtil.pad(SUtil.getOrDefault(this.shape, 2, "   "), 3);
        String[] ls = new String[]{l1, l2, l3};
        for (int i = 0; i < ls.length; ++i) {
            String[] lps = ls[i].split("");
            for (int j = 0; j < lps.length; ++j) {
                materials[i][j] = this.ingredientMap.getOrDefault(Character.valueOf(lps[j].charAt(0)), new MaterialQuantifiable(SMaterial.AIR, 1));
            }
        }
        return materials;
    }

    protected static ShapedRecipe parseShapedRecipe(ItemStack[] stacks) {
        if (stacks.length != 9) {
            throw new UnsupportedOperationException("Recipe parsing requires a 9 element array!");
        }
        MaterialQuantifiable[] l1 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 0, 3));
        MaterialQuantifiable[] l2 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 3, 6));
        MaterialQuantifiable[] l3 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 6, 9));
        MaterialQuantifiable[][] grid = ShapedRecipe.airless(new MaterialQuantifiable[][]{l1, l2, l3});
        MaterialQuantifiable[] seg = ShapedRecipe.segment(MaterialQuantifiable.of(stacks));
        for (ShapedRecipe recipe : CACHED_RECIPES) {
            MaterialQuantifiable[][] airRecipeGrid = recipe.toMQ2DArray();
            MaterialQuantifiable[][] recipeGrid = ShapedRecipe.airless(airRecipeGrid);
            MaterialQuantifiable[] recipeSeg = ShapedRecipe.segment(SUtil.unnest(airRecipeGrid, MaterialQuantifiable.class));
            if (!ShapedRecipe.recipeAccepted(recipe.useExchangeables, grid, recipeGrid) || !ShapedRecipe.recipeAccepted(recipe.useExchangeables, seg, recipeSeg)) continue;
            return recipe;
        }
        return null;
    }

    private static <T> boolean deepSameLength(T[][] a1, T[][] a2) {
        int c1 = 0;
        int c2 = 0;
        for (T[] a : a1) {
            c1 += a.length;
        }
        for (T[] a : a2) {
            c2 += a.length;
        }
        return c1 == c2;
    }

    private static MaterialQuantifiable[] segment(MaterialQuantifiable[] materials) {
        int firstNonAir = -1;
        int lastNonAir = -1;
        for (int i = 0; i < materials.length; ++i) {
            MaterialQuantifiable material = materials[i];
            if (firstNonAir == -1 && material.getMaterial() != SMaterial.AIR) {
                firstNonAir = i;
            }
            if (material.getMaterial() == SMaterial.AIR) continue;
            lastNonAir = i;
        }
        if (firstNonAir == -1 || lastNonAir == -1) {
            return new MaterialQuantifiable[0];
        }
        return Arrays.copyOfRange(materials, firstNonAir, lastNonAir + 1);
    }

    private static boolean recipeAccepted(boolean usesExchangeables, MaterialQuantifiable[][] grid, MaterialQuantifiable[][] recipeGrid) {
        if (!ShapedRecipe.deepSameLength(grid, recipeGrid)) {
            return false;
        }
        boolean found = true;
        try {
            for (int i = 0; i < grid.length; ++i) {
                for (int j = 0; j < grid[i].length; ++j) {
                    MaterialQuantifiable m1 = grid[i][j];
                    MaterialQuantifiable m2 = recipeGrid[i][j];
                    List<SMaterial> exchangeables = ShapedRecipe.getExchangeablesOf(m2.getMaterial());
                    if (usesExchangeables && exchangeables != null && exchangeables.contains((Object)m1.getMaterial()) && m1.getAmount() >= m2.getAmount() || m1.getMaterial() == m2.getMaterial() && m1.getAmount() >= m2.getAmount()) continue;
                    found = false;
                    break;
                }
                if (found) {
                    continue;
                }
                break;
            }
        }
        catch (IndexOutOfBoundsException ex) {
            return false;
        }
        return found;
    }

    private static boolean recipeAccepted(boolean usesExchangeables, MaterialQuantifiable[] grid1d, MaterialQuantifiable[] recipeGrid1d) {
        if (grid1d.length != recipeGrid1d.length) {
            return false;
        }
        boolean found = true;
        for (int i = 0; i < grid1d.length; ++i) {
            MaterialQuantifiable m1 = grid1d[i];
            MaterialQuantifiable m2 = recipeGrid1d[i];
            List<SMaterial> exchangeables = ShapedRecipe.getExchangeablesOf(m2.getMaterial());
            if (usesExchangeables && exchangeables != null && exchangeables.contains((Object)m1.getMaterial()) && m1.getAmount() >= m2.getAmount() || m1.getMaterial() == m2.getMaterial() && m1.getAmount() >= m2.getAmount()) continue;
            found = false;
            break;
        }
        return found;
    }

    public String[] getShape() {
        return this.shape;
    }

    public Map<Character, MaterialQuantifiable> getIngredientMap() {
        return this.ingredientMap;
    }
}
