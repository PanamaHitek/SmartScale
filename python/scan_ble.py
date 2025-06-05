import asyncio
import warnings
import json
from bleak import BleakScanner

# Suppress deprecation warnings from bleak
warnings.filterwarnings("ignore", category=FutureWarning)

TARGET_NAME = "SC02"
SCAN_TIMEOUT = 5.0

async def scan_by_name():
    try:
        devices = await asyncio.wait_for(
            BleakScanner.discover(timeout=SCAN_TIMEOUT),
            timeout=SCAN_TIMEOUT + 1
        )
        matched = [
            {
                "address": d.address,
                "name": d.name or "Unknown",
                "rssi": d.details.rssi if hasattr(d, "details") and hasattr(d.details, "rssi") else d.rssi
            }
            for d in devices
            if TARGET_NAME is None or (d.name and d.name == TARGET_NAME)
        ]
        return {
            "found": bool(matched),
            "devices": matched
        }
    except asyncio.TimeoutError:
        return {
            "found": False,
            "devices": []
        }

def main():
    return asyncio.run(scan_by_name())

if __name__ == "__main__":
    result = main()
    print(json.dumps(result, indent=2))
