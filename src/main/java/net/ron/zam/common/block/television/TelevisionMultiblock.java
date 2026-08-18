package net.ron.zam.common.block.television;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.ron.zam.registry.ZAMBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public final class TelevisionMultiblock {
    private static final ThreadLocal<Boolean> REFRESHING = ThreadLocal.withInitial(() -> false);

    private TelevisionMultiblock() {}

    public static void refresh(Level level, BlockPos pos, Direction facing) {
        if (REFRESHING.get()) return;

        REFRESHING.set(true);
        try {
            refreshGroupAt(level, pos, facing);
            refreshNeighbors(level, pos, facing);
        } finally {
            REFRESHING.set(false);
        }
    }

    public static void refreshNeighbors(Level level, BlockPos pos, Direction facing) {
        boolean owns = !REFRESHING.get();
        if (owns) REFRESHING.set(true);

        try {
            Direction left = facing.getClockWise();
            Direction right = facing.getCounterClockWise();

            refreshGroupAt(level, pos.relative(left), facing);
            refreshGroupAt(level, pos.relative(right), facing);
            refreshGroupAt(level, pos.above(), facing);
            refreshGroupAt(level, pos.below(), facing);
        } finally {
            if (owns) REFRESHING.set(false);
        }
    }

    public static void transferRemovedPlayback(Level level, BlockPos removedPos, Direction facing,
                                               ItemStack cassette, boolean playing, int ticks) {
        if (level.isClientSide() || cassette.isEmpty()) return;

        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        Set<BlockPos> seen = new HashSet<>();
        Set<BlockPos> best = null;

        for (BlockPos neighbor : new BlockPos[]{
                removedPos.relative(left),
                removedPos.relative(right),
                removedPos.above(),
                removedPos.below()
        }) {
            if (!isMatchingTv(level, neighbor, facing) || seen.contains(neighbor))
                continue;

            Set<BlockPos> group = collectGrid(level, neighbor, facing);
            seen.addAll(group);

            if (best == null || group.size() > best.size()
                    || group.size() == best.size() && compareGroups(group, best) < 0)
                best = group;
        }

        if (best == null || best.isEmpty()) return;

        BlockPos targetPos = null;

        for (BlockPos pos : best) {
            if (!(level.getBlockEntity(pos) instanceof TelevisionBlockEntity tv)
                    || !tv.cassette().isEmpty())
                continue;

            if (targetPos == null || comparePos(pos, targetPos) < 0)
                targetPos = pos;
        }

        if (targetPos == null) return;

        if (level.getBlockEntity(targetPos) instanceof TelevisionBlockEntity target) {
            target.transferPlayback(cassette, playing, ticks);
            refreshGroupAt(level, targetPos, facing);
        }
    }

    private static int compareGroups(Set<BlockPos> a, Set<BlockPos> b) {
        BlockPos pa = canonicalPos(a), pb = canonicalPos(b);

        if (pa == null) return pb == null ? 0 : 1;
        if (pb == null) return -1;

        return comparePos(pa, pb);
    }

    @Nullable
    private static BlockPos canonicalPos(Set<BlockPos> group) {
        BlockPos best = null;

        for (BlockPos pos : group)
            if (best == null || comparePos(pos, best) < 0)
                best = pos;

        return best;
    }

    private static void refreshGroupAt(Level level, BlockPos pos, Direction facing) {
        if (!level.getBlockState(pos).is(ZAMBlocks.TELEVISION)) return;

        Set<BlockPos> grid = collectGrid(level, pos, facing);
        if (grid.isEmpty()) return;

        Rectangle rect = findLargestRectangle(grid, facing);

        for (BlockPos p : grid)
            updateWithRect(level, p, facing, rect);
    }

    private static void updateWithRect(Level level, BlockPos pos, Direction facing, @Nullable Rectangle rect) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(ZAMBlocks.TELEVISION)) return;

        TelevisionBlock.Connection code = TelevisionBlock.Connection.SINGLE;

        if (rect != null) {
            int c = col(pos, facing), r = row(pos);

            if (rect.contains(c, r)) {
                boolean above = rect.contains(c, r - 1);
                boolean below = rect.contains(c, r + 1);
                boolean left = rect.contains(c - 1, r);
                boolean right = rect.contains(c + 1, r);

                code = computeConnection(above, below, left, right);
            }
        }

        if (state.getValue(TelevisionBlock.CONNECTION) != code)
            level.setBlockAndUpdate(pos, state.setValue(TelevisionBlock.CONNECTION, code));
    }

    private static TelevisionBlock.Connection computeConnection(boolean above, boolean below,
                                                                boolean left, boolean right) {
        int mask = (above ? 1 : 0) | (below ? 2 : 0) | (left ? 4 : 0) | (right ? 8 : 0);

        return switch (mask) {
            case 15 -> TelevisionBlock.Connection.CENTER;
            case 14 -> TelevisionBlock.Connection.TOP;
            case 13 -> TelevisionBlock.Connection.BOTTOM;
            case 11 -> TelevisionBlock.Connection.LEFT;
            case 7 -> TelevisionBlock.Connection.RIGHT;
            case 10 -> TelevisionBlock.Connection.TOP_LEFT;
            case 6 -> TelevisionBlock.Connection.TOP_RIGHT;
            case 9 -> TelevisionBlock.Connection.BOTTOM_LEFT;
            case 5 -> TelevisionBlock.Connection.BOTTOM_RIGHT;
            case 8 -> TelevisionBlock.Connection.HORIZONTAL_LEFT;
            case 12 -> TelevisionBlock.Connection.HORIZONTAL_MIDDLE;
            case 4 -> TelevisionBlock.Connection.HORIZONTAL_RIGHT;
            case 2 -> TelevisionBlock.Connection.VERTICAL_TOP;
            case 3 -> TelevisionBlock.Connection.VERTICAL_MIDDLE;
            case 1 -> TelevisionBlock.Connection.VERTICAL_BOTTOM;
            default -> TelevisionBlock.Connection.SINGLE;
        };
    }

    private static Set<BlockPos> collectGrid(Level level, BlockPos start, Direction facing) {
        Set<BlockPos> grid = new HashSet<>();
        if (!isMatchingTv(level, start, facing)) return grid;

        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);
        grid.add(start);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            for (BlockPos neighbor : new BlockPos[]{
                    current.above(),
                    current.below(),
                    current.relative(left),
                    current.relative(right)
            }) {
                if (grid.add(neighbor)) {
                    if (isMatchingTv(level, neighbor, facing))
                        queue.add(neighbor);
                    else
                        grid.remove(neighbor);
                }
            }
        }

        return grid;
    }

    private static boolean isMatchingTv(Level level, BlockPos pos, Direction facing) {
        return isMatchingTv(level.getBlockState(pos), facing);
    }

    private static boolean isMatchingTv(BlockState state, Direction facing) {
        return state.is(ZAMBlocks.TELEVISION)
                && state.hasProperty(TelevisionBlock.FACING)
                && state.getValue(TelevisionBlock.FACING) == facing;
    }

    public record MasterTV(BlockPos pos, TelevisionBlockEntity tv) {}

    public static boolean isAnyConnectedTvPowered(Level level, BlockPos start, Direction facing) {
        Set<BlockPos> grid = collectGrid(level, start, facing);
        Rectangle rect = findLargestRectangle(grid, facing);

        int c = col(start, facing), r = row(start);

        if (rect != null && rect.contains(c, r)) {
            for (BlockPos p : grid) {
                if (!rect.contains(col(p, facing), row(p))) continue;

                BlockState state = level.getBlockState(p);

                if (state.is(ZAMBlocks.TELEVISION)
                        && state.getValue(TelevisionBlock.POWER_STATE).isOn())
                    return true;
            }

            return false;
        }

        BlockState state = level.getBlockState(start);

        return state.is(ZAMBlocks.TELEVISION)
                && state.getValue(TelevisionBlock.POWER_STATE).isOn();
    }

    @Nullable
    public static MasterTV findMaster(Level level, BlockPos start, Direction facing) {
        TelevisionBlockEntity startBe =
                level.getBlockEntity(start) instanceof TelevisionBlockEntity be ? be : null;

        if (startBe == null) return null;

        Set<BlockPos> grid = collectGrid(level, start, facing);
        Rectangle rect = findLargestRectangle(grid, facing);

        int c = col(start, facing), r = row(start);
        BlockPos bestPos = null;
        TelevisionBlockEntity bestBe = null;

        if (rect != null && rect.contains(c, r)) {
            for (BlockPos p : grid) {
                if (!rect.contains(col(p, facing), row(p))) continue;

                if (level.getBlockEntity(p) instanceof TelevisionBlockEntity tv
                        && !tv.cassette().isEmpty()
                        && (bestPos == null || comparePos(p, bestPos) < 0)) {
                    bestPos = p;
                    bestBe = tv;
                }
            }
        } else if (!startBe.cassette().isEmpty()) {
            return new MasterTV(start, startBe);
        }

        return bestBe != null
                ? new MasterTV(bestPos, bestBe)
                : new MasterTV(start, startBe);
    }

    private static int comparePos(BlockPos a, BlockPos b) {
        int dy = Integer.compare(a.getY(), b.getY());
        if (dy != 0) return dy;

        int dx = Integer.compare(a.getX(), b.getX());
        if (dx != 0) return dx;

        return Integer.compare(a.getZ(), b.getZ());
    }

    public record GridSlot(int col, int row, int totalCols, int totalRows) {}

    public static GridSlot getGridSlot(Level level, BlockPos start, Direction facing) {
        Set<BlockPos> grid = collectGrid(level, start, facing);
        Rectangle rect = findLargestRectangle(grid, facing);

        int c = col(start, facing), r = row(start);

        if (rect == null || !rect.contains(c, r))
            return new GridSlot(0, 0, 1, 1);

        return new GridSlot(
                c - rect.minCol(),
                r - rect.minRow(),
                rect.cols(),
                rect.rows()
        );
    }

    private static int col(BlockPos pos, Direction facing) {
        Direction right = facing.getCounterClockWise();

        return pos.getX() * right.getStepX()
                + pos.getZ() * right.getStepZ();
    }

    private static int row(BlockPos pos) {
        return -pos.getY();
    }

    public record Rectangle(int minCol, int maxCol, int minRow, int maxRow) {
        public int cols() { return maxCol - minCol + 1; }
        public int rows() { return maxRow - minRow + 1; }
        public int area() { return cols() * rows(); }

        public boolean contains(int col, int row) {
            return col >= minCol && col <= maxCol
                    && row >= minRow && row <= maxRow;
        }
    }

    @Nullable
    private static Rectangle findLargestRectangle(Set<BlockPos> grid, Direction facing) {
        if (grid.isEmpty()) return null;

        Set<Long> filled = new HashSet<>();
        int minCol = Integer.MAX_VALUE, maxCol = Integer.MIN_VALUE;
        int minRow = Integer.MAX_VALUE, maxRow = Integer.MIN_VALUE;

        for (BlockPos p : grid) {
            int c = col(p, facing), r = row(p);

            filled.add(packColRow(c, r));
            minCol = Math.min(minCol, c);
            maxCol = Math.max(maxCol, c);
            minRow = Math.min(minRow, r);
            maxRow = Math.max(maxRow, r);
        }

        int width = maxCol - minCol + 1;
        int height = maxRow - minRow + 1;
        boolean[][] cell = new boolean[height][width];

        for (long key : filled) {
            int c = (int) (key >> 32);
            int r = (int) key;
            cell[r - minRow][c - minCol] = true;
        }

        int[] heights = new int[width];
        int bestArea = 0;
        Rectangle best = null;

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++)
                heights[c] = cell[r][c] ? heights[c] + 1 : 0;

            Rectangle candidate =
                    largestRectInHistogram(heights, r, minCol, minRow);

            if (candidate != null && candidate.area() > bestArea) {
                bestArea = candidate.area();
                best = candidate;
            }
        }

        return best;
    }

    @Nullable
    private static Rectangle largestRectInHistogram(int[] heights, int baseRow,
                                                    int minColOffset, int minRowOffset) {
        Deque<Integer> stack = new ArrayDeque<>();
        int bestArea = 0;
        Rectangle best = null;

        for (int i = 0; i <= heights.length; i++) {
            int h = i == heights.length ? 0 : heights[i];

            while (!stack.isEmpty() && heights[stack.peek()] > h) {
                int top = stack.pop();
                int topH = heights[top];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                int area = topH * width;

                if (area > bestArea) {
                    bestArea = area;

                    int left = stack.isEmpty() ? 0 : stack.peek() + 1;
                    int right = i - 1;
                    int topRow = baseRow - topH + 1;

                    best = new Rectangle(
                            left + minColOffset,
                            right + minColOffset,
                            topRow + minRowOffset,
                            baseRow + minRowOffset
                    );
                }
            }

            stack.push(i);
        }

        return best;
    }

    private static long packColRow(int col, int row) {
        return ((long) col << 32) | (row & 0xFFFFFFFFL);
    }
}