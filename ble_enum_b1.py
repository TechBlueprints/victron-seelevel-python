import asyncio
import sys

ADDR = "05:23:01:64:00:C3"


def ensure_bleak_on_path() -> None:
    try:
        import bleak  # noqa: F401
        return
    except Exception:
        pass
    # Try bundled bleak from dbus-serialbattery ext path
    sys.path.insert(0, "/data/apps/dbus-serialbattery/ext")
    sys.path.insert(0, "/data/apps/dbus-serialbattery/ext/bleak")


async def try_adapter(address: str, adapter: str | None) -> None:
    from bleak import BleakClient  # imported after path setup

    label = adapter if adapter else "default"
    print(f"=== adapter: {label} ===", flush=True)
    try:
        client = BleakClient(address, adapter=adapter, timeout=12.0)
        await client.connect()
        # is_connected is a property on many bleak versions
        try:
            connected = client.is_connected  # type: ignore[attr-defined]
            if callable(connected):
                connected = connected()
        except Exception:
            connected = True
        print(f"connected: {connected}", flush=True)
        # Probe well-known UUIDs without relying on get_services
        probes = {
            "model_number": "00002a24-0000-1000-8000-00805f9b34fb",
            "manufacturer": "00002a29-0000-1000-8000-00805f9b34fb",
            "ffe1": "0000ffe1-0000-1000-8000-00805f9b34fb",
            "ffe2": "0000ffe2-0000-1000-8000-00805f9b34fb",
            "nus_rx": "6e400003-b5a3-f393-e0a9-e50e24dcca9e",
            "nus_tx": "6e400002-b5a3-f393-e0a9-e50e24dcca9e",
        }
        # Read model/manufacturer if present
        for name in ("model_number", "manufacturer", "ffe1"):
            uuid = probes[name]
            try:
                data = await client.read_gatt_char(uuid)
                try:
                    text = data.decode(errors="ignore")
                except Exception:
                    text = data.hex()
                print(f"READ {name} {uuid}: {text}")
            except Exception as re:
                print(f"READ {name} {uuid}: ERR {re}")
        # Try enabling notify on NUS RX
        try:
            async def _cb(_sender, _data):
                print(f"NOTIFY nus_rx {_data.hex()}")
            await client.start_notify(probes["nus_rx"], _cb)
            print("NUS notify started")
            await asyncio.sleep(0.2)
            await client.stop_notify(probes["nus_rx"])
            print("NUS notify stopped")
        except Exception as ne:
            print(f"NUS notify failed: {ne}")
        await client.disconnect()
    except Exception as e:
        print(f"ERROR on {label}: {e}")


async def main() -> None:
    ensure_bleak_on_path()
    for adapter in ["hci0", "hci1", None]:
        await try_adapter(ADDR, adapter)


if __name__ == "__main__":
    asyncio.run(main())


