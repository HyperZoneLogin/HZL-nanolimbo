/*
 * This file is part of HyperZoneLogin, licensed under the GNU Affero General Public License v3.0 or later.
 *
 * Copyright (C) ksqeib (庆灵) <ksqeib@qq.com>
 * Copyright (C) contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package icu.h2l.login.backend.nanolimbo

import com.velocitypowered.api.command.CommandSource
import icu.h2l.api.message.HyperZoneMessagePlaceholder
import icu.h2l.api.message.HyperZoneMessageServiceProvider
import net.kyori.adventure.text.Component

/**
 * backend-nanolimbo 模块的本地化文案入口。
 *
 * 所有面向管理员的命令输出都必须经过这里，避免在命令实现中散落硬编码字符串。
 */
object NanoLimboMessages {
    private const val NAMESPACE = "backend-nanolimbo"

    fun usageStatus(source: CommandSource): Component = render(source, "usage.status")
    fun usageStart(source: CommandSource): Component = render(source, "usage.start")
    fun usageStop(source: CommandSource): Component = render(source, "usage.stop")
    fun usageRestart(source: CommandSource): Component = render(source, "usage.restart")
    fun usageConnections(source: CommandSource): Component =
        render(source, "usage.connections")

    fun statusRunning(source: CommandSource): Component = render(source, "status.running")
    fun statusStopped(source: CommandSource): Component = render(source, "status.stopped")

    fun statusAddress(source: CommandSource, address: String): Component =
        render(source, "status.address", HyperZoneMessagePlaceholder.text("address", address))

    fun statusUptime(source: CommandSource, uptime: String): Component =
        render(source, "status.uptime", HyperZoneMessagePlaceholder.text("uptime", uptime))

    fun statusConnections(source: CommandSource, count: Int, maxPlayers: Int): Component = render(source, "status.connections",
        HyperZoneMessagePlaceholder.text("count", count),
        HyperZoneMessagePlaceholder.text("max_players", maxPlayers)
    )

    fun statusTransport(source: CommandSource, transport: String): Component =
        render(source, "status.transport", HyperZoneMessagePlaceholder.text("transport", transport))

    fun statusVersion(source: CommandSource, version: String): Component =
        render(source, "status.version", HyperZoneMessagePlaceholder.text("version", version))

    fun statusLastFailure(source: CommandSource, reason: String): Component =
        render(source, "status.last-failure", HyperZoneMessagePlaceholder.text("reason", reason))

    fun startStarting(source: CommandSource): Component = render(source, "start.starting")

    fun startSuccess(source: CommandSource, address: String): Component =
        render(source, "start.success", HyperZoneMessagePlaceholder.text("address", address))

    fun startFailed(source: CommandSource, reason: String): Component =
        render(source, "start.failed", HyperZoneMessagePlaceholder.text("reason", reason))

    fun startAlreadyRunning(source: CommandSource): Component =
        render(source, "start.already-running")

    fun stopStopping(source: CommandSource): Component = render(source, "stop.stopping")
    fun stopSuccess(source: CommandSource): Component = render(source, "stop.success")
    fun stopNotRunning(source: CommandSource): Component = render(source, "stop.not-running")

    fun stopFailed(source: CommandSource, reason: String): Component =
        render(source, "stop.failed", HyperZoneMessagePlaceholder.text("reason", reason))

    fun restartRestarting(source: CommandSource): Component =
        render(source, "restart.restarting")

    fun restartSuccess(source: CommandSource, address: String): Component =
        render(source, "restart.success", HyperZoneMessagePlaceholder.text("address", address))

    fun restartFailed(source: CommandSource, reason: String): Component =
        render(source, "restart.failed", HyperZoneMessagePlaceholder.text("reason", reason))

    fun connectionsSummary(source: CommandSource, count: Int): Component =
        render(source, "connections.summary", HyperZoneMessagePlaceholder.text("count", count))

    fun connectionsEmpty(source: CommandSource): Component = render(source, "connections.empty")

    fun connectionsEntry(
        source: CommandSource,
        index: Int,
        name: String,
        uuid: String,
        clientVersion: String,
        address: String,
    ): Component = render(source, "connections.entry",
        HyperZoneMessagePlaceholder.text("index", index),
        HyperZoneMessagePlaceholder.text("name", name),
        HyperZoneMessagePlaceholder.text("uuid", uuid),
        HyperZoneMessagePlaceholder.text("client_version", clientVersion),
        HyperZoneMessagePlaceholder.text("player_address", address)
    )

    fun connectionsAddressRedacted(source: CommandSource): Component =
        render(source, "connections.address-redacted")

    fun busy(source: CommandSource): Component =
        render(source, "busy")

    /**
     * 把毫秒时长格式化为 `1h 2m 3s` 形式，未满一秒时给出 `<1s`。
     */
    fun formatDuration(millis: Long): String {
        if (millis <= 0L) {
            return "0s"
        }
        if (millis < 1000L) {
            return "<1s"
        }

        val totalSeconds = millis / 1000L
        val hours = totalSeconds / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L

        return buildString {
            if (hours > 0L) append("${hours}h ")
            if (hours > 0L || minutes > 0L) append("${minutes}m ")
            append("${seconds}s")
        }
    }

    private fun render(
        source: CommandSource,
        key: String,
        vararg placeholders: HyperZoneMessagePlaceholder
    ): Component {
        return HyperZoneMessageServiceProvider.get().render(source, "$NAMESPACE.$key", *placeholders)
    }
}
